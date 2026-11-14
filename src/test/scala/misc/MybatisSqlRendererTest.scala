package misc

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.{assertTrue, assertFalse, assertThrows}

/**
 * MybatisSqlRenderer 单元测试。
 * 覆盖：参数收集、<if> true/false 渲染、#{} 内联、#{null} -> NULL、${} 内联、
 *       choose/when/otherwise、foreach。
 *
 * 注：MyBatis 渲染 foreach 时会把每项拆到独立行并带缩进，故用 compress() 压缩空白后再断言，
 *     避免格式差异导致的假失败。
 */
class MybatisSqlRendererTest {

  /** 压缩连续空白（含换行）为单个空格，用于空格不敏感的断言。 */
  private def compress(s: String): String = s.replaceAll("\\s+", " ")

  private val simpleIf: String =
    """SELECT *
      |FROM anneng_ads.ads_station_merge_contract_dt
      |WHERE 1 = 1
      |-- @formatter:off
      |<if test="agent_id != null">
      |    and station_settlement_agent_id = #{agent_id}
      |</if>
      |-- @formatter:on
      |and is_received = '未签收'
      |""".stripMargin

  @Test
  def testCollectParams(): Unit = {
    val params = MybatisSqlRenderer.collectParams(simpleIf)
    assertTrue(params.contains("agent_id"), s"应收集到 agent_id，实际: $params")
  }

  @Test
  def testRenderIfTrue(): Unit = {
    val out = MybatisSqlRenderer.render(simpleIf, Map("agent_id" -> 879L))
    assertTrue(out.contains("and station_settlement_agent_id = 879"), s"应内联参数值，实际: \n$out")
    assertFalse(out.contains("<if"), "不应保留 <if> 标签")
    assertFalse(out.contains("#{"), "不应保留 #{} 占位")
  }

  @Test
  def testRenderIfFalseRemovesBlock(): Unit = {
    // null 参数 -> <if test="agent_id != null"> 块被丢弃
    val out = MybatisSqlRenderer.render(simpleIf, Map("agent_id" -> null))
    assertTrue(out.contains("is_received = '未签收'"), "应保留 if 外内容")
    assertFalse(out.contains("agent_id ="), "if 块内容应被删除")
    assertFalse(out.contains("<if"), "不应保留 <if> 标签")
  }

  @Test
  def testRenderStringParamQuoted(): Unit = {
    val sql = "SELECT * FROM t WHERE name = #{name}"
    val out = MybatisSqlRenderer.render(sql, Map("name" -> "张三's"))
    assertTrue(out.contains("name = '张三''s'"), s"字符串应加引号并转义，实际: $out")
  }

  @Test
  def testRenderNullParamToNull(): Unit = {
    val sql = "SELECT * FROM t WHERE a = #{x}"
    val out = MybatisSqlRenderer.render(sql, Map("x" -> null))
    assertTrue(out.contains("a = NULL"), s"null 参数应渲染为 NULL，实际: $out")
  }

  @Test
  def testRenderDollarParamInlined(): Unit = {
    val sql = "SELECT * FROM ${tbl} WHERE a = 1"
    val out = MybatisSqlRenderer.render(sql, Map("tbl" -> "my_table"))
    assertTrue(out.contains("FROM my_table"), s"${} 应直接内联，实际: $out")
  }

  @Test
  def testRenderChooseWhen(): Unit = {
    val sql =
      """SELECT * FROM t
        |<choose>
        |    <when test="id != null">
        |        WHERE id = #{id}
        |    </when>
        |    <otherwise>
        |        WHERE 1 = 0
        |    </otherwise>
        |</choose>
        |""".stripMargin
    val withId = MybatisSqlRenderer.render(sql, Map("id" -> 5L))
    assertTrue(compress(withId).contains("WHERE id = 5"), s"when 命中时应走 when 分支，实际: \n$withId")
    assertFalse(withId.contains("<choose"), "不应保留 choose 标签")

    val withoutId = MybatisSqlRenderer.render(sql, Map("id" -> null))
    assertTrue(compress(withoutId).contains("WHERE 1 = 0"), s"when 不命中时应走 otherwise，实际: \n$withoutId")
  }

  @Test
  def testRenderForeach(): Unit = {
    val sql =
      """SELECT * FROM t
        |WHERE id IN
        |<foreach collection="ids" item="id" open="(" separator="," close=")">
        |    #{id}
        |</foreach>
        |""".stripMargin
    val out = MybatisSqlRenderer.render(sql, Map("ids" -> java.util.Arrays.asList(1, 2, 3, "x")))
    // MyBatis 会把 foreach 各项拆行并带缩进，用压缩空白 + 正则对空格不敏感地校验顺序与值
    assertTrue(compress(out).matches(".*IN \\( 1 , 2 , 3 , 'x' \\).*"),
      s"foreach 应展开为 IN (1,2,3,'x')，实际: \n$out")
    assertFalse(out.contains("<foreach"), "不应保留 foreach 标签")
  }

  @Test
  def testRenderLessThanOperator(): Unit = {
    // 裸 < 会破坏 XML，渲染器应自动转义，最终 SQL 仍保留 <
    val sql = "SELECT * FROM t WHERE a < 5 AND b > 1"
    val out = MybatisSqlRenderer.render(sql, Map.empty[String, Any])
    assertTrue(out.contains("a < 5"), s"小于号应原样保留，实际: $out")
    assertFalse(out.contains("&lt;"), s"不应把转义实体泄漏到结果里，实际: $out")
  }

  @Test
  def testRenderAmpersand(): Unit = {
    val sql = "SELECT * FROM t WHERE a = 1 AND b & 2 = 0"
    val out = MybatisSqlRenderer.render(sql, Map.empty[String, Any])
    assertTrue(out.contains("b & 2"), s"& 应原样保留，实际: $out")
    assertFalse(out.contains("&amp;"), s"不应把转义实体泄漏到结果里，实际: $out")
  }

  @Test
  def testRenderIfWithLessThanInSql(): Unit = {
    // 动态标签 + SQL 内比较符共存
    val sql =
      """SELECT * FROM t WHERE 1 = 1
        |<if test="d != null">
        |    and dt &lt;= #{d}
        |</if>
        |and a < 10
        |""".stripMargin
    val out = MybatisSqlRenderer.render(sql, Map("d" -> "2026-01-01"))
    val c = compress(out)
    assertTrue(c.contains("dt <= '2026-01-01'"), s"实体与参数都应正确渲染，实际: \n$out")
    assertTrue(c.contains("a < 10"), s"裸 < 也应保留，实际: \n$out")
  }

  @Test
  def testGuardedForeachWithNullCollectionRemovesBlock(): Unit = {
    val sql =
      """SELECT * FROM t
        |<where>
        |  <if test="ids != null">
        |    id IN
        |    <foreach collection="ids" item="id" open="(" separator="," close=")">
        |      #{id}
        |    </foreach>
        |  </if>
        |</where>
        |""".stripMargin
    // ids 为 null：<if> 判空后整块删除，不应残留孤立的 "IN"
    val out = MybatisSqlRenderer.render(sql, Map("ids" -> null))
    assertFalse(out.contains("<foreach"), "不应保留 foreach 标签")
    assertFalse(out.contains("<where"), "不应保留 where 标签")
    assertFalse(compress(out).contains("IN"), s"不应残留孤立的 IN，实际: \n$out")
  }

  @Test
  def testUnguardedForeachWithNullCollectionGivesFriendlyError(): Unit = {
    val sql =
      """SELECT * FROM t WHERE id IN
        |<foreach collection="ids" item="id" open="(" separator="," close=")">
        |  #{id}
        |</foreach>
        |""".stripMargin
    val ex = assertThrows(classOf[IllegalStateException], () => {
      MybatisSqlRenderer.render(sql, Map("ids" -> null)); ()
    })
    assertTrue(ex.getMessage.contains("foreach 的集合参数"),
      s"应提示 foreach 集合参数未赋值，实际: ${ex.getMessage}")
  }

  @Test
  def testWhereTagTrimsLeadingAnd(): Unit = {
    val sql =
      """SELECT * FROM t
        |<where>
        |  <if test="a != null">and a = #{a}</if>
        |  <if test="b != null">and b = #{b}</if>
        |</where>
        |""".stripMargin
    val out = MybatisSqlRenderer.render(sql, Map("a" -> 1L, "b" -> null))
    val c = compress(out)
    assertTrue(c.contains("WHERE a = 1"), s"where 应去掉开头多余的 and，实际: \n$out")
    assertFalse(c.contains("and WHERE"), s"不应出现 and WHERE，实际: \n$out")
  }

  @Test
  def testTidyCollapsesBlankLines(): Unit = {
    val sql =
      """SELECT * FROM t
        |
        |
        |WHERE 1 = 1
        |""".stripMargin
    val out = MybatisSqlRenderer.render(sql, Map.empty[String, Any])
    assertFalse(out.contains("\n\n\n"), s"连续空行应被压缩，实际: [$out]")
  }

  @Test
  def testFriendlyErrorOnUnclosedTag(): Unit = {
    val sql = "SELECT * FROM t <if test=\"a != null\"> and a = #{a}"
    val ex = assertThrows(classOf[IllegalStateException], () => {
      MybatisSqlRenderer.render(sql, Map("a" -> 1L)); ()
    })
    assertTrue(ex.getMessage.contains("可能原因"), s"应给出友好提示，实际: ${ex.getMessage}")
  }
}