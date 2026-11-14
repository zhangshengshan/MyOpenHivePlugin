package misc

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.{assertTrue, assertFalse}

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
}