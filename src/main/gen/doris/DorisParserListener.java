package doris;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link DorisParser}.
 */
public interface DorisParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link DorisParser#multiStatements}.
	 * @param ctx the parse tree
	 */
	void enterMultiStatements(DorisParser.MultiStatementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#multiStatements}.
	 * @param ctx the parse tree
	 */
	void exitMultiStatements(DorisParser.MultiStatementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#singleStatement}.
	 * @param ctx the parse tree
	 */
	void enterSingleStatement(DorisParser.SingleStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#singleStatement}.
	 * @param ctx the parse tree
	 */
	void exitSingleStatement(DorisParser.SingleStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementDefault}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementDefault(DorisParser.StatementDefaultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementDefault}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementDefault(DorisParser.StatementDefaultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createRowPolicy}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCreateRowPolicy(DorisParser.CreateRowPolicyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createRowPolicy}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCreateRowPolicy(DorisParser.CreateRowPolicyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createTable}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCreateTable(DorisParser.CreateTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createTable}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCreateTable(DorisParser.CreateTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code insertTable}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterInsertTable(DorisParser.InsertTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code insertTable}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitInsertTable(DorisParser.InsertTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code update}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterUpdate(DorisParser.UpdateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code update}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitUpdate(DorisParser.UpdateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code delete}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDelete(DorisParser.DeleteContext ctx);
	/**
	 * Exit a parse tree produced by the {@code delete}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDelete(DorisParser.DeleteContext ctx);
	/**
	 * Enter a parse tree produced by the {@code load}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterLoad(DorisParser.LoadContext ctx);
	/**
	 * Exit a parse tree produced by the {@code load}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitLoad(DorisParser.LoadContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mysqlLoad}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterMysqlLoad(DorisParser.MysqlLoadContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mysqlLoad}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitMysqlLoad(DorisParser.MysqlLoadContext ctx);
	/**
	 * Enter a parse tree produced by the {@code export}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterExport(DorisParser.ExportContext ctx);
	/**
	 * Exit a parse tree produced by the {@code export}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitExport(DorisParser.ExportContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createMTMV}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCreateMTMV(DorisParser.CreateMTMVContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createMTMV}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCreateMTMV(DorisParser.CreateMTMVContext ctx);
	/**
	 * Enter a parse tree produced by the {@code refreshMTMV}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterRefreshMTMV(DorisParser.RefreshMTMVContext ctx);
	/**
	 * Exit a parse tree produced by the {@code refreshMTMV}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitRefreshMTMV(DorisParser.RefreshMTMVContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterMTMV}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterAlterMTMV(DorisParser.AlterMTMVContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterMTMV}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitAlterMTMV(DorisParser.AlterMTMVContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropMTMV}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDropMTMV(DorisParser.DropMTMVContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropMTMV}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDropMTMV(DorisParser.DropMTMVContext ctx);
	/**
	 * Enter a parse tree produced by the {@code pauseMTMV}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterPauseMTMV(DorisParser.PauseMTMVContext ctx);
	/**
	 * Exit a parse tree produced by the {@code pauseMTMV}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitPauseMTMV(DorisParser.PauseMTMVContext ctx);
	/**
	 * Enter a parse tree produced by the {@code resumeMTMV}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterResumeMTMV(DorisParser.ResumeMTMVContext ctx);
	/**
	 * Exit a parse tree produced by the {@code resumeMTMV}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitResumeMTMV(DorisParser.ResumeMTMVContext ctx);
	/**
	 * Enter a parse tree produced by the {@code cancelMTMVTask}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCancelMTMVTask(DorisParser.CancelMTMVTaskContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cancelMTMVTask}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCancelMTMVTask(DorisParser.CancelMTMVTaskContext ctx);
	/**
	 * Enter a parse tree produced by the {@code addConstraint}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterAddConstraint(DorisParser.AddConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code addConstraint}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitAddConstraint(DorisParser.AddConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropConstraint}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDropConstraint(DorisParser.DropConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropConstraint}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDropConstraint(DorisParser.DropConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showConstraint}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowConstraint(DorisParser.ShowConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showConstraint}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowConstraint(DorisParser.ShowConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code callProcedure}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCallProcedure(DorisParser.CallProcedureContext ctx);
	/**
	 * Exit a parse tree produced by the {@code callProcedure}
	 * labeled alternative in {@link DorisParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCallProcedure(DorisParser.CallProcedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#constraint}.
	 * @param ctx the parse tree
	 */
	void enterConstraint(DorisParser.ConstraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#constraint}.
	 * @param ctx the parse tree
	 */
	void exitConstraint(DorisParser.ConstraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#partitionSpec}.
	 * @param ctx the parse tree
	 */
	void enterPartitionSpec(DorisParser.PartitionSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#partitionSpec}.
	 * @param ctx the parse tree
	 */
	void exitPartitionSpec(DorisParser.PartitionSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#dataDesc}.
	 * @param ctx the parse tree
	 */
	void enterDataDesc(DorisParser.DataDescContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#dataDesc}.
	 * @param ctx the parse tree
	 */
	void exitDataDesc(DorisParser.DataDescContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#buildMode}.
	 * @param ctx the parse tree
	 */
	void enterBuildMode(DorisParser.BuildModeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#buildMode}.
	 * @param ctx the parse tree
	 */
	void exitBuildMode(DorisParser.BuildModeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#refreshTrigger}.
	 * @param ctx the parse tree
	 */
	void enterRefreshTrigger(DorisParser.RefreshTriggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#refreshTrigger}.
	 * @param ctx the parse tree
	 */
	void exitRefreshTrigger(DorisParser.RefreshTriggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#refreshSchedule}.
	 * @param ctx the parse tree
	 */
	void enterRefreshSchedule(DorisParser.RefreshScheduleContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#refreshSchedule}.
	 * @param ctx the parse tree
	 */
	void exitRefreshSchedule(DorisParser.RefreshScheduleContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#refreshMethod}.
	 * @param ctx the parse tree
	 */
	void enterRefreshMethod(DorisParser.RefreshMethodContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#refreshMethod}.
	 * @param ctx the parse tree
	 */
	void exitRefreshMethod(DorisParser.RefreshMethodContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#identifierOrStringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierOrStringLiteral(DorisParser.IdentifierOrStringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#identifierOrStringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierOrStringLiteral(DorisParser.IdentifierOrStringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#identifierOrText}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierOrText(DorisParser.IdentifierOrTextContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#identifierOrText}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierOrText(DorisParser.IdentifierOrTextContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#userIdentify}.
	 * @param ctx the parse tree
	 */
	void enterUserIdentify(DorisParser.UserIdentifyContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#userIdentify}.
	 * @param ctx the parse tree
	 */
	void exitUserIdentify(DorisParser.UserIdentifyContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#explain}.
	 * @param ctx the parse tree
	 */
	void enterExplain(DorisParser.ExplainContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#explain}.
	 * @param ctx the parse tree
	 */
	void exitExplain(DorisParser.ExplainContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#planType}.
	 * @param ctx the parse tree
	 */
	void enterPlanType(DorisParser.PlanTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#planType}.
	 * @param ctx the parse tree
	 */
	void exitPlanType(DorisParser.PlanTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#mergeType}.
	 * @param ctx the parse tree
	 */
	void enterMergeType(DorisParser.MergeTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#mergeType}.
	 * @param ctx the parse tree
	 */
	void exitMergeType(DorisParser.MergeTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#preFilterClause}.
	 * @param ctx the parse tree
	 */
	void enterPreFilterClause(DorisParser.PreFilterClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#preFilterClause}.
	 * @param ctx the parse tree
	 */
	void exitPreFilterClause(DorisParser.PreFilterClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#deleteOnClause}.
	 * @param ctx the parse tree
	 */
	void enterDeleteOnClause(DorisParser.DeleteOnClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#deleteOnClause}.
	 * @param ctx the parse tree
	 */
	void exitDeleteOnClause(DorisParser.DeleteOnClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#sequenceColClause}.
	 * @param ctx the parse tree
	 */
	void enterSequenceColClause(DorisParser.SequenceColClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#sequenceColClause}.
	 * @param ctx the parse tree
	 */
	void exitSequenceColClause(DorisParser.SequenceColClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#colFromPath}.
	 * @param ctx the parse tree
	 */
	void enterColFromPath(DorisParser.ColFromPathContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#colFromPath}.
	 * @param ctx the parse tree
	 */
	void exitColFromPath(DorisParser.ColFromPathContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#colMappingList}.
	 * @param ctx the parse tree
	 */
	void enterColMappingList(DorisParser.ColMappingListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#colMappingList}.
	 * @param ctx the parse tree
	 */
	void exitColMappingList(DorisParser.ColMappingListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#mappingExpr}.
	 * @param ctx the parse tree
	 */
	void enterMappingExpr(DorisParser.MappingExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#mappingExpr}.
	 * @param ctx the parse tree
	 */
	void exitMappingExpr(DorisParser.MappingExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#withRemoteStorageSystem}.
	 * @param ctx the parse tree
	 */
	void enterWithRemoteStorageSystem(DorisParser.WithRemoteStorageSystemContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#withRemoteStorageSystem}.
	 * @param ctx the parse tree
	 */
	void exitWithRemoteStorageSystem(DorisParser.WithRemoteStorageSystemContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#resourceDesc}.
	 * @param ctx the parse tree
	 */
	void enterResourceDesc(DorisParser.ResourceDescContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#resourceDesc}.
	 * @param ctx the parse tree
	 */
	void exitResourceDesc(DorisParser.ResourceDescContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#mysqlDataDesc}.
	 * @param ctx the parse tree
	 */
	void enterMysqlDataDesc(DorisParser.MysqlDataDescContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#mysqlDataDesc}.
	 * @param ctx the parse tree
	 */
	void exitMysqlDataDesc(DorisParser.MysqlDataDescContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#skipLines}.
	 * @param ctx the parse tree
	 */
	void enterSkipLines(DorisParser.SkipLinesContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#skipLines}.
	 * @param ctx the parse tree
	 */
	void exitSkipLines(DorisParser.SkipLinesContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#outFileClause}.
	 * @param ctx the parse tree
	 */
	void enterOutFileClause(DorisParser.OutFileClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#outFileClause}.
	 * @param ctx the parse tree
	 */
	void exitOutFileClause(DorisParser.OutFileClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#query}.
	 * @param ctx the parse tree
	 */
	void enterQuery(DorisParser.QueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#query}.
	 * @param ctx the parse tree
	 */
	void exitQuery(DorisParser.QueryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code queryTermDefault}
	 * labeled alternative in {@link DorisParser#queryTerm}.
	 * @param ctx the parse tree
	 */
	void enterQueryTermDefault(DorisParser.QueryTermDefaultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code queryTermDefault}
	 * labeled alternative in {@link DorisParser#queryTerm}.
	 * @param ctx the parse tree
	 */
	void exitQueryTermDefault(DorisParser.QueryTermDefaultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setOperation}
	 * labeled alternative in {@link DorisParser#queryTerm}.
	 * @param ctx the parse tree
	 */
	void enterSetOperation(DorisParser.SetOperationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setOperation}
	 * labeled alternative in {@link DorisParser#queryTerm}.
	 * @param ctx the parse tree
	 */
	void exitSetOperation(DorisParser.SetOperationContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#setQuantifier}.
	 * @param ctx the parse tree
	 */
	void enterSetQuantifier(DorisParser.SetQuantifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#setQuantifier}.
	 * @param ctx the parse tree
	 */
	void exitSetQuantifier(DorisParser.SetQuantifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code queryPrimaryDefault}
	 * labeled alternative in {@link DorisParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void enterQueryPrimaryDefault(DorisParser.QueryPrimaryDefaultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code queryPrimaryDefault}
	 * labeled alternative in {@link DorisParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void exitQueryPrimaryDefault(DorisParser.QueryPrimaryDefaultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subquery}
	 * labeled alternative in {@link DorisParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void enterSubquery(DorisParser.SubqueryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subquery}
	 * labeled alternative in {@link DorisParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void exitSubquery(DorisParser.SubqueryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code valuesTable}
	 * labeled alternative in {@link DorisParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void enterValuesTable(DorisParser.ValuesTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code valuesTable}
	 * labeled alternative in {@link DorisParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void exitValuesTable(DorisParser.ValuesTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code regularQuerySpecification}
	 * labeled alternative in {@link DorisParser#querySpecification}.
	 * @param ctx the parse tree
	 */
	void enterRegularQuerySpecification(DorisParser.RegularQuerySpecificationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code regularQuerySpecification}
	 * labeled alternative in {@link DorisParser#querySpecification}.
	 * @param ctx the parse tree
	 */
	void exitRegularQuerySpecification(DorisParser.RegularQuerySpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#cte}.
	 * @param ctx the parse tree
	 */
	void enterCte(DorisParser.CteContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#cte}.
	 * @param ctx the parse tree
	 */
	void exitCte(DorisParser.CteContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#aliasQuery}.
	 * @param ctx the parse tree
	 */
	void enterAliasQuery(DorisParser.AliasQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#aliasQuery}.
	 * @param ctx the parse tree
	 */
	void exitAliasQuery(DorisParser.AliasQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#columnAliases}.
	 * @param ctx the parse tree
	 */
	void enterColumnAliases(DorisParser.ColumnAliasesContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#columnAliases}.
	 * @param ctx the parse tree
	 */
	void exitColumnAliases(DorisParser.ColumnAliasesContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#selectClause}.
	 * @param ctx the parse tree
	 */
	void enterSelectClause(DorisParser.SelectClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#selectClause}.
	 * @param ctx the parse tree
	 */
	void exitSelectClause(DorisParser.SelectClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#selectColumnClause}.
	 * @param ctx the parse tree
	 */
	void enterSelectColumnClause(DorisParser.SelectColumnClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#selectColumnClause}.
	 * @param ctx the parse tree
	 */
	void exitSelectColumnClause(DorisParser.SelectColumnClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#whereClause}.
	 * @param ctx the parse tree
	 */
	void enterWhereClause(DorisParser.WhereClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#whereClause}.
	 * @param ctx the parse tree
	 */
	void exitWhereClause(DorisParser.WhereClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#fromClause}.
	 * @param ctx the parse tree
	 */
	void enterFromClause(DorisParser.FromClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#fromClause}.
	 * @param ctx the parse tree
	 */
	void exitFromClause(DorisParser.FromClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#relation}.
	 * @param ctx the parse tree
	 */
	void enterRelation(DorisParser.RelationContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#relation}.
	 * @param ctx the parse tree
	 */
	void exitRelation(DorisParser.RelationContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#joinRelation}.
	 * @param ctx the parse tree
	 */
	void enterJoinRelation(DorisParser.JoinRelationContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#joinRelation}.
	 * @param ctx the parse tree
	 */
	void exitJoinRelation(DorisParser.JoinRelationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bracketDistributeType}
	 * labeled alternative in {@link DorisParser#distributeType}.
	 * @param ctx the parse tree
	 */
	void enterBracketDistributeType(DorisParser.BracketDistributeTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bracketDistributeType}
	 * labeled alternative in {@link DorisParser#distributeType}.
	 * @param ctx the parse tree
	 */
	void exitBracketDistributeType(DorisParser.BracketDistributeTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code commentDistributeType}
	 * labeled alternative in {@link DorisParser#distributeType}.
	 * @param ctx the parse tree
	 */
	void enterCommentDistributeType(DorisParser.CommentDistributeTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code commentDistributeType}
	 * labeled alternative in {@link DorisParser#distributeType}.
	 * @param ctx the parse tree
	 */
	void exitCommentDistributeType(DorisParser.CommentDistributeTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bracketRelationHint}
	 * labeled alternative in {@link DorisParser#relationHint}.
	 * @param ctx the parse tree
	 */
	void enterBracketRelationHint(DorisParser.BracketRelationHintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bracketRelationHint}
	 * labeled alternative in {@link DorisParser#relationHint}.
	 * @param ctx the parse tree
	 */
	void exitBracketRelationHint(DorisParser.BracketRelationHintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code commentRelationHint}
	 * labeled alternative in {@link DorisParser#relationHint}.
	 * @param ctx the parse tree
	 */
	void enterCommentRelationHint(DorisParser.CommentRelationHintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code commentRelationHint}
	 * labeled alternative in {@link DorisParser#relationHint}.
	 * @param ctx the parse tree
	 */
	void exitCommentRelationHint(DorisParser.CommentRelationHintContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#aggClause}.
	 * @param ctx the parse tree
	 */
	void enterAggClause(DorisParser.AggClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#aggClause}.
	 * @param ctx the parse tree
	 */
	void exitAggClause(DorisParser.AggClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#groupingElement}.
	 * @param ctx the parse tree
	 */
	void enterGroupingElement(DorisParser.GroupingElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#groupingElement}.
	 * @param ctx the parse tree
	 */
	void exitGroupingElement(DorisParser.GroupingElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#groupingSet}.
	 * @param ctx the parse tree
	 */
	void enterGroupingSet(DorisParser.GroupingSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#groupingSet}.
	 * @param ctx the parse tree
	 */
	void exitGroupingSet(DorisParser.GroupingSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#havingClause}.
	 * @param ctx the parse tree
	 */
	void enterHavingClause(DorisParser.HavingClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#havingClause}.
	 * @param ctx the parse tree
	 */
	void exitHavingClause(DorisParser.HavingClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#selectHint}.
	 * @param ctx the parse tree
	 */
	void enterSelectHint(DorisParser.SelectHintContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#selectHint}.
	 * @param ctx the parse tree
	 */
	void exitSelectHint(DorisParser.SelectHintContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#hintStatement}.
	 * @param ctx the parse tree
	 */
	void enterHintStatement(DorisParser.HintStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#hintStatement}.
	 * @param ctx the parse tree
	 */
	void exitHintStatement(DorisParser.HintStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#hintAssignment}.
	 * @param ctx the parse tree
	 */
	void enterHintAssignment(DorisParser.HintAssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#hintAssignment}.
	 * @param ctx the parse tree
	 */
	void exitHintAssignment(DorisParser.HintAssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#updateAssignment}.
	 * @param ctx the parse tree
	 */
	void enterUpdateAssignment(DorisParser.UpdateAssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#updateAssignment}.
	 * @param ctx the parse tree
	 */
	void exitUpdateAssignment(DorisParser.UpdateAssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#updateAssignmentSeq}.
	 * @param ctx the parse tree
	 */
	void enterUpdateAssignmentSeq(DorisParser.UpdateAssignmentSeqContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#updateAssignmentSeq}.
	 * @param ctx the parse tree
	 */
	void exitUpdateAssignmentSeq(DorisParser.UpdateAssignmentSeqContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#lateralView}.
	 * @param ctx the parse tree
	 */
	void enterLateralView(DorisParser.LateralViewContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#lateralView}.
	 * @param ctx the parse tree
	 */
	void exitLateralView(DorisParser.LateralViewContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#queryOrganization}.
	 * @param ctx the parse tree
	 */
	void enterQueryOrganization(DorisParser.QueryOrganizationContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#queryOrganization}.
	 * @param ctx the parse tree
	 */
	void exitQueryOrganization(DorisParser.QueryOrganizationContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#sortClause}.
	 * @param ctx the parse tree
	 */
	void enterSortClause(DorisParser.SortClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#sortClause}.
	 * @param ctx the parse tree
	 */
	void exitSortClause(DorisParser.SortClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#sortItem}.
	 * @param ctx the parse tree
	 */
	void enterSortItem(DorisParser.SortItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#sortItem}.
	 * @param ctx the parse tree
	 */
	void exitSortItem(DorisParser.SortItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#limitClause}.
	 * @param ctx the parse tree
	 */
	void enterLimitClause(DorisParser.LimitClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#limitClause}.
	 * @param ctx the parse tree
	 */
	void exitLimitClause(DorisParser.LimitClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#partitionClause}.
	 * @param ctx the parse tree
	 */
	void enterPartitionClause(DorisParser.PartitionClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#partitionClause}.
	 * @param ctx the parse tree
	 */
	void exitPartitionClause(DorisParser.PartitionClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#joinType}.
	 * @param ctx the parse tree
	 */
	void enterJoinType(DorisParser.JoinTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#joinType}.
	 * @param ctx the parse tree
	 */
	void exitJoinType(DorisParser.JoinTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#joinCriteria}.
	 * @param ctx the parse tree
	 */
	void enterJoinCriteria(DorisParser.JoinCriteriaContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#joinCriteria}.
	 * @param ctx the parse tree
	 */
	void exitJoinCriteria(DorisParser.JoinCriteriaContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#identifierList}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierList(DorisParser.IdentifierListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#identifierList}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierList(DorisParser.IdentifierListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#identifierSeq}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierSeq(DorisParser.IdentifierSeqContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#identifierSeq}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierSeq(DorisParser.IdentifierSeqContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableName}
	 * labeled alternative in {@link DorisParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void enterTableName(DorisParser.TableNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableName}
	 * labeled alternative in {@link DorisParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void exitTableName(DorisParser.TableNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code aliasedQuery}
	 * labeled alternative in {@link DorisParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void enterAliasedQuery(DorisParser.AliasedQueryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code aliasedQuery}
	 * labeled alternative in {@link DorisParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void exitAliasedQuery(DorisParser.AliasedQueryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableValuedFunction}
	 * labeled alternative in {@link DorisParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void enterTableValuedFunction(DorisParser.TableValuedFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableValuedFunction}
	 * labeled alternative in {@link DorisParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void exitTableValuedFunction(DorisParser.TableValuedFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#propertyClause}.
	 * @param ctx the parse tree
	 */
	void enterPropertyClause(DorisParser.PropertyClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#propertyClause}.
	 * @param ctx the parse tree
	 */
	void exitPropertyClause(DorisParser.PropertyClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#propertyItemList}.
	 * @param ctx the parse tree
	 */
	void enterPropertyItemList(DorisParser.PropertyItemListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#propertyItemList}.
	 * @param ctx the parse tree
	 */
	void exitPropertyItemList(DorisParser.PropertyItemListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#propertyItem}.
	 * @param ctx the parse tree
	 */
	void enterPropertyItem(DorisParser.PropertyItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#propertyItem}.
	 * @param ctx the parse tree
	 */
	void exitPropertyItem(DorisParser.PropertyItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#propertyKey}.
	 * @param ctx the parse tree
	 */
	void enterPropertyKey(DorisParser.PropertyKeyContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#propertyKey}.
	 * @param ctx the parse tree
	 */
	void exitPropertyKey(DorisParser.PropertyKeyContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#propertyValue}.
	 * @param ctx the parse tree
	 */
	void enterPropertyValue(DorisParser.PropertyValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#propertyValue}.
	 * @param ctx the parse tree
	 */
	void exitPropertyValue(DorisParser.PropertyValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#tableAlias}.
	 * @param ctx the parse tree
	 */
	void enterTableAlias(DorisParser.TableAliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#tableAlias}.
	 * @param ctx the parse tree
	 */
	void exitTableAlias(DorisParser.TableAliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#multipartIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterMultipartIdentifier(DorisParser.MultipartIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#multipartIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitMultipartIdentifier(DorisParser.MultipartIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#simpleColumnDefs}.
	 * @param ctx the parse tree
	 */
	void enterSimpleColumnDefs(DorisParser.SimpleColumnDefsContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#simpleColumnDefs}.
	 * @param ctx the parse tree
	 */
	void exitSimpleColumnDefs(DorisParser.SimpleColumnDefsContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#simpleColumnDef}.
	 * @param ctx the parse tree
	 */
	void enterSimpleColumnDef(DorisParser.SimpleColumnDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#simpleColumnDef}.
	 * @param ctx the parse tree
	 */
	void exitSimpleColumnDef(DorisParser.SimpleColumnDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#columnDefs}.
	 * @param ctx the parse tree
	 */
	void enterColumnDefs(DorisParser.ColumnDefsContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#columnDefs}.
	 * @param ctx the parse tree
	 */
	void exitColumnDefs(DorisParser.ColumnDefsContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#columnDef}.
	 * @param ctx the parse tree
	 */
	void enterColumnDef(DorisParser.ColumnDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#columnDef}.
	 * @param ctx the parse tree
	 */
	void exitColumnDef(DorisParser.ColumnDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#indexDefs}.
	 * @param ctx the parse tree
	 */
	void enterIndexDefs(DorisParser.IndexDefsContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#indexDefs}.
	 * @param ctx the parse tree
	 */
	void exitIndexDefs(DorisParser.IndexDefsContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#indexDef}.
	 * @param ctx the parse tree
	 */
	void enterIndexDef(DorisParser.IndexDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#indexDef}.
	 * @param ctx the parse tree
	 */
	void exitIndexDef(DorisParser.IndexDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#partitionsDef}.
	 * @param ctx the parse tree
	 */
	void enterPartitionsDef(DorisParser.PartitionsDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#partitionsDef}.
	 * @param ctx the parse tree
	 */
	void exitPartitionsDef(DorisParser.PartitionsDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#partitionDef}.
	 * @param ctx the parse tree
	 */
	void enterPartitionDef(DorisParser.PartitionDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#partitionDef}.
	 * @param ctx the parse tree
	 */
	void exitPartitionDef(DorisParser.PartitionDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#lessThanPartitionDef}.
	 * @param ctx the parse tree
	 */
	void enterLessThanPartitionDef(DorisParser.LessThanPartitionDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#lessThanPartitionDef}.
	 * @param ctx the parse tree
	 */
	void exitLessThanPartitionDef(DorisParser.LessThanPartitionDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#fixedPartitionDef}.
	 * @param ctx the parse tree
	 */
	void enterFixedPartitionDef(DorisParser.FixedPartitionDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#fixedPartitionDef}.
	 * @param ctx the parse tree
	 */
	void exitFixedPartitionDef(DorisParser.FixedPartitionDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#stepPartitionDef}.
	 * @param ctx the parse tree
	 */
	void enterStepPartitionDef(DorisParser.StepPartitionDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#stepPartitionDef}.
	 * @param ctx the parse tree
	 */
	void exitStepPartitionDef(DorisParser.StepPartitionDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#inPartitionDef}.
	 * @param ctx the parse tree
	 */
	void enterInPartitionDef(DorisParser.InPartitionDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#inPartitionDef}.
	 * @param ctx the parse tree
	 */
	void exitInPartitionDef(DorisParser.InPartitionDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#constantSeq}.
	 * @param ctx the parse tree
	 */
	void enterConstantSeq(DorisParser.ConstantSeqContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#constantSeq}.
	 * @param ctx the parse tree
	 */
	void exitConstantSeq(DorisParser.ConstantSeqContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#partitionValueDef}.
	 * @param ctx the parse tree
	 */
	void enterPartitionValueDef(DorisParser.PartitionValueDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#partitionValueDef}.
	 * @param ctx the parse tree
	 */
	void exitPartitionValueDef(DorisParser.PartitionValueDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#rollupDefs}.
	 * @param ctx the parse tree
	 */
	void enterRollupDefs(DorisParser.RollupDefsContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#rollupDefs}.
	 * @param ctx the parse tree
	 */
	void exitRollupDefs(DorisParser.RollupDefsContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#rollupDef}.
	 * @param ctx the parse tree
	 */
	void enterRollupDef(DorisParser.RollupDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#rollupDef}.
	 * @param ctx the parse tree
	 */
	void exitRollupDef(DorisParser.RollupDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#aggTypeDef}.
	 * @param ctx the parse tree
	 */
	void enterAggTypeDef(DorisParser.AggTypeDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#aggTypeDef}.
	 * @param ctx the parse tree
	 */
	void exitAggTypeDef(DorisParser.AggTypeDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#tabletList}.
	 * @param ctx the parse tree
	 */
	void enterTabletList(DorisParser.TabletListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#tabletList}.
	 * @param ctx the parse tree
	 */
	void exitTabletList(DorisParser.TabletListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#inlineTable}.
	 * @param ctx the parse tree
	 */
	void enterInlineTable(DorisParser.InlineTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#inlineTable}.
	 * @param ctx the parse tree
	 */
	void exitInlineTable(DorisParser.InlineTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#namedExpression}.
	 * @param ctx the parse tree
	 */
	void enterNamedExpression(DorisParser.NamedExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#namedExpression}.
	 * @param ctx the parse tree
	 */
	void exitNamedExpression(DorisParser.NamedExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#namedExpressionSeq}.
	 * @param ctx the parse tree
	 */
	void enterNamedExpressionSeq(DorisParser.NamedExpressionSeqContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#namedExpressionSeq}.
	 * @param ctx the parse tree
	 */
	void exitNamedExpressionSeq(DorisParser.NamedExpressionSeqContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(DorisParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(DorisParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#lambdaExpression}.
	 * @param ctx the parse tree
	 */
	void enterLambdaExpression(DorisParser.LambdaExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#lambdaExpression}.
	 * @param ctx the parse tree
	 */
	void exitLambdaExpression(DorisParser.LambdaExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exist}
	 * labeled alternative in {@link DorisParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterExist(DorisParser.ExistContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exist}
	 * labeled alternative in {@link DorisParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitExist(DorisParser.ExistContext ctx);
	/**
	 * Enter a parse tree produced by the {@code logicalNot}
	 * labeled alternative in {@link DorisParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalNot(DorisParser.LogicalNotContext ctx);
	/**
	 * Exit a parse tree produced by the {@code logicalNot}
	 * labeled alternative in {@link DorisParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalNot(DorisParser.LogicalNotContext ctx);
	/**
	 * Enter a parse tree produced by the {@code predicated}
	 * labeled alternative in {@link DorisParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterPredicated(DorisParser.PredicatedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code predicated}
	 * labeled alternative in {@link DorisParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitPredicated(DorisParser.PredicatedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code isnull}
	 * labeled alternative in {@link DorisParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterIsnull(DorisParser.IsnullContext ctx);
	/**
	 * Exit a parse tree produced by the {@code isnull}
	 * labeled alternative in {@link DorisParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitIsnull(DorisParser.IsnullContext ctx);
	/**
	 * Enter a parse tree produced by the {@code is_not_null_pred}
	 * labeled alternative in {@link DorisParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterIs_not_null_pred(DorisParser.Is_not_null_predContext ctx);
	/**
	 * Exit a parse tree produced by the {@code is_not_null_pred}
	 * labeled alternative in {@link DorisParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitIs_not_null_pred(DorisParser.Is_not_null_predContext ctx);
	/**
	 * Enter a parse tree produced by the {@code logicalBinary}
	 * labeled alternative in {@link DorisParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalBinary(DorisParser.LogicalBinaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code logicalBinary}
	 * labeled alternative in {@link DorisParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalBinary(DorisParser.LogicalBinaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code doublePipes}
	 * labeled alternative in {@link DorisParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterDoublePipes(DorisParser.DoublePipesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code doublePipes}
	 * labeled alternative in {@link DorisParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitDoublePipes(DorisParser.DoublePipesContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#rowConstructor}.
	 * @param ctx the parse tree
	 */
	void enterRowConstructor(DorisParser.RowConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#rowConstructor}.
	 * @param ctx the parse tree
	 */
	void exitRowConstructor(DorisParser.RowConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#rowConstructorItem}.
	 * @param ctx the parse tree
	 */
	void enterRowConstructorItem(DorisParser.RowConstructorItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#rowConstructorItem}.
	 * @param ctx the parse tree
	 */
	void exitRowConstructorItem(DorisParser.RowConstructorItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterPredicate(DorisParser.PredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitPredicate(DorisParser.PredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code valueExpressionDefault}
	 * labeled alternative in {@link DorisParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void enterValueExpressionDefault(DorisParser.ValueExpressionDefaultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code valueExpressionDefault}
	 * labeled alternative in {@link DorisParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void exitValueExpressionDefault(DorisParser.ValueExpressionDefaultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code comparison}
	 * labeled alternative in {@link DorisParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void enterComparison(DorisParser.ComparisonContext ctx);
	/**
	 * Exit a parse tree produced by the {@code comparison}
	 * labeled alternative in {@link DorisParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void exitComparison(DorisParser.ComparisonContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bitOperation}
	 * labeled alternative in {@link DorisParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void enterBitOperation(DorisParser.BitOperationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bitOperation}
	 * labeled alternative in {@link DorisParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void exitBitOperation(DorisParser.BitOperationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arithmeticBinary}
	 * labeled alternative in {@link DorisParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void enterArithmeticBinary(DorisParser.ArithmeticBinaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arithmeticBinary}
	 * labeled alternative in {@link DorisParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void exitArithmeticBinary(DorisParser.ArithmeticBinaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arithmeticUnary}
	 * labeled alternative in {@link DorisParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void enterArithmeticUnary(DorisParser.ArithmeticUnaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arithmeticUnary}
	 * labeled alternative in {@link DorisParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void exitArithmeticUnary(DorisParser.ArithmeticUnaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#datetimeUnit}.
	 * @param ctx the parse tree
	 */
	void enterDatetimeUnit(DorisParser.DatetimeUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#datetimeUnit}.
	 * @param ctx the parse tree
	 */
	void exitDatetimeUnit(DorisParser.DatetimeUnitContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dateCeil}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterDateCeil(DorisParser.DateCeilContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dateCeil}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitDateCeil(DorisParser.DateCeilContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dereference}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterDereference(DorisParser.DereferenceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dereference}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitDereference(DorisParser.DereferenceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code timestampadd}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterTimestampadd(DorisParser.TimestampaddContext ctx);
	/**
	 * Exit a parse tree produced by the {@code timestampadd}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitTimestampadd(DorisParser.TimestampaddContext ctx);
	/**
	 * Enter a parse tree produced by the {@code date_sub}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterDate_sub(DorisParser.Date_subContext ctx);
	/**
	 * Exit a parse tree produced by the {@code date_sub}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitDate_sub(DorisParser.Date_subContext ctx);
	/**
	 * Enter a parse tree produced by the {@code cast}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterCast(DorisParser.CastContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cast}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitCast(DorisParser.CastContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenthesizedExpression}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenthesizedExpression(DorisParser.ParenthesizedExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenthesizedExpression}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenthesizedExpression(DorisParser.ParenthesizedExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code userVariable}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterUserVariable(DorisParser.UserVariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code userVariable}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitUserVariable(DorisParser.UserVariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code elementAt}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterElementAt(DorisParser.ElementAtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code elementAt}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitElementAt(DorisParser.ElementAtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code charFunction}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterCharFunction(DorisParser.CharFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code charFunction}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitCharFunction(DorisParser.CharFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intervalLiteral}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterIntervalLiteral(DorisParser.IntervalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intervalLiteral}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitIntervalLiteral(DorisParser.IntervalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleCase}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterSimpleCase(DorisParser.SimpleCaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleCase}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitSimpleCase(DorisParser.SimpleCaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code columnReference}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterColumnReference(DorisParser.ColumnReferenceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code columnReference}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitColumnReference(DorisParser.ColumnReferenceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code star}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterStar(DorisParser.StarContext ctx);
	/**
	 * Exit a parse tree produced by the {@code star}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitStar(DorisParser.StarContext ctx);
	/**
	 * Enter a parse tree produced by the {@code convertType}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterConvertType(DorisParser.ConvertTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code convertType}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitConvertType(DorisParser.ConvertTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code timestampdiff}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterTimestampdiff(DorisParser.TimestampdiffContext ctx);
	/**
	 * Exit a parse tree produced by the {@code timestampdiff}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitTimestampdiff(DorisParser.TimestampdiffContext ctx);
	/**
	 * Enter a parse tree produced by the {@code convertCharSet}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterConvertCharSet(DorisParser.ConvertCharSetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code convertCharSet}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitConvertCharSet(DorisParser.ConvertCharSetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subqueryExpression}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterSubqueryExpression(DorisParser.SubqueryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subqueryExpression}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitSubqueryExpression(DorisParser.SubqueryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code encryptKey}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterEncryptKey(DorisParser.EncryptKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code encryptKey}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitEncryptKey(DorisParser.EncryptKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code date_add}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterDate_add(DorisParser.Date_addContext ctx);
	/**
	 * Exit a parse tree produced by the {@code date_add}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitDate_add(DorisParser.Date_addContext ctx);
	/**
	 * Enter a parse tree produced by the {@code systemVariable}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterSystemVariable(DorisParser.SystemVariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code systemVariable}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitSystemVariable(DorisParser.SystemVariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code collate}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterCollate(DorisParser.CollateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code collate}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitCollate(DorisParser.CollateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constantDefault}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterConstantDefault(DorisParser.ConstantDefaultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constantDefault}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitConstantDefault(DorisParser.ConstantDefaultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code extract}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterExtract(DorisParser.ExtractContext ctx);
	/**
	 * Exit a parse tree produced by the {@code extract}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitExtract(DorisParser.ExtractContext ctx);
	/**
	 * Enter a parse tree produced by the {@code functionCall}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(DorisParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code functionCall}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(DorisParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arraySlice}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterArraySlice(DorisParser.ArraySliceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arraySlice}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitArraySlice(DorisParser.ArraySliceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dateFloor}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterDateFloor(DorisParser.DateFloorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dateFloor}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitDateFloor(DorisParser.DateFloorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code searchedCase}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterSearchedCase(DorisParser.SearchedCaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code searchedCase}
	 * labeled alternative in {@link DorisParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitSearchedCase(DorisParser.SearchedCaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#functionCallExpression}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCallExpression(DorisParser.FunctionCallExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#functionCallExpression}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCallExpression(DorisParser.FunctionCallExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#functionIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterFunctionIdentifier(DorisParser.FunctionIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#functionIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitFunctionIdentifier(DorisParser.FunctionIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#functionNameIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterFunctionNameIdentifier(DorisParser.FunctionNameIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#functionNameIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitFunctionNameIdentifier(DorisParser.FunctionNameIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#windowSpec}.
	 * @param ctx the parse tree
	 */
	void enterWindowSpec(DorisParser.WindowSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#windowSpec}.
	 * @param ctx the parse tree
	 */
	void exitWindowSpec(DorisParser.WindowSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#windowFrame}.
	 * @param ctx the parse tree
	 */
	void enterWindowFrame(DorisParser.WindowFrameContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#windowFrame}.
	 * @param ctx the parse tree
	 */
	void exitWindowFrame(DorisParser.WindowFrameContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#frameUnits}.
	 * @param ctx the parse tree
	 */
	void enterFrameUnits(DorisParser.FrameUnitsContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#frameUnits}.
	 * @param ctx the parse tree
	 */
	void exitFrameUnits(DorisParser.FrameUnitsContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#frameBoundary}.
	 * @param ctx the parse tree
	 */
	void enterFrameBoundary(DorisParser.FrameBoundaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#frameBoundary}.
	 * @param ctx the parse tree
	 */
	void exitFrameBoundary(DorisParser.FrameBoundaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedName(DorisParser.QualifiedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedName(DorisParser.QualifiedNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#specifiedPartition}.
	 * @param ctx the parse tree
	 */
	void enterSpecifiedPartition(DorisParser.SpecifiedPartitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#specifiedPartition}.
	 * @param ctx the parse tree
	 */
	void exitSpecifiedPartition(DorisParser.SpecifiedPartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nullLiteral}
	 * labeled alternative in {@link DorisParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterNullLiteral(DorisParser.NullLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nullLiteral}
	 * labeled alternative in {@link DorisParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitNullLiteral(DorisParser.NullLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code typeConstructor}
	 * labeled alternative in {@link DorisParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterTypeConstructor(DorisParser.TypeConstructorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typeConstructor}
	 * labeled alternative in {@link DorisParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitTypeConstructor(DorisParser.TypeConstructorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code numericLiteral}
	 * labeled alternative in {@link DorisParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterNumericLiteral(DorisParser.NumericLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code numericLiteral}
	 * labeled alternative in {@link DorisParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitNumericLiteral(DorisParser.NumericLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code booleanLiteral}
	 * labeled alternative in {@link DorisParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterBooleanLiteral(DorisParser.BooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code booleanLiteral}
	 * labeled alternative in {@link DorisParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitBooleanLiteral(DorisParser.BooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code stringLiteral}
	 * labeled alternative in {@link DorisParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteral(DorisParser.StringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code stringLiteral}
	 * labeled alternative in {@link DorisParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteral(DorisParser.StringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayLiteral}
	 * labeled alternative in {@link DorisParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterArrayLiteral(DorisParser.ArrayLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayLiteral}
	 * labeled alternative in {@link DorisParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitArrayLiteral(DorisParser.ArrayLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mapLiteral}
	 * labeled alternative in {@link DorisParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterMapLiteral(DorisParser.MapLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mapLiteral}
	 * labeled alternative in {@link DorisParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitMapLiteral(DorisParser.MapLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code structLiteral}
	 * labeled alternative in {@link DorisParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterStructLiteral(DorisParser.StructLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code structLiteral}
	 * labeled alternative in {@link DorisParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitStructLiteral(DorisParser.StructLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void enterComparisonOperator(DorisParser.ComparisonOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void exitComparisonOperator(DorisParser.ComparisonOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#booleanValue}.
	 * @param ctx the parse tree
	 */
	void enterBooleanValue(DorisParser.BooleanValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#booleanValue}.
	 * @param ctx the parse tree
	 */
	void exitBooleanValue(DorisParser.BooleanValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#whenClause}.
	 * @param ctx the parse tree
	 */
	void enterWhenClause(DorisParser.WhenClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#whenClause}.
	 * @param ctx the parse tree
	 */
	void exitWhenClause(DorisParser.WhenClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#interval}.
	 * @param ctx the parse tree
	 */
	void enterInterval(DorisParser.IntervalContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#interval}.
	 * @param ctx the parse tree
	 */
	void exitInterval(DorisParser.IntervalContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#unitIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterUnitIdentifier(DorisParser.UnitIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#unitIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitUnitIdentifier(DorisParser.UnitIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code complexDataType}
	 * labeled alternative in {@link DorisParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterComplexDataType(DorisParser.ComplexDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code complexDataType}
	 * labeled alternative in {@link DorisParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitComplexDataType(DorisParser.ComplexDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code primitiveDataType}
	 * labeled alternative in {@link DorisParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveDataType(DorisParser.PrimitiveDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code primitiveDataType}
	 * labeled alternative in {@link DorisParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveDataType(DorisParser.PrimitiveDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#primitiveColType}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveColType(DorisParser.PrimitiveColTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#primitiveColType}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveColType(DorisParser.PrimitiveColTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#complexColTypeList}.
	 * @param ctx the parse tree
	 */
	void enterComplexColTypeList(DorisParser.ComplexColTypeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#complexColTypeList}.
	 * @param ctx the parse tree
	 */
	void exitComplexColTypeList(DorisParser.ComplexColTypeListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#complexColType}.
	 * @param ctx the parse tree
	 */
	void enterComplexColType(DorisParser.ComplexColTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#complexColType}.
	 * @param ctx the parse tree
	 */
	void exitComplexColType(DorisParser.ComplexColTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#commentSpec}.
	 * @param ctx the parse tree
	 */
	void enterCommentSpec(DorisParser.CommentSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#commentSpec}.
	 * @param ctx the parse tree
	 */
	void exitCommentSpec(DorisParser.CommentSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#sample}.
	 * @param ctx the parse tree
	 */
	void enterSample(DorisParser.SampleContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#sample}.
	 * @param ctx the parse tree
	 */
	void exitSample(DorisParser.SampleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code sampleByPercentile}
	 * labeled alternative in {@link DorisParser#sampleMethod}.
	 * @param ctx the parse tree
	 */
	void enterSampleByPercentile(DorisParser.SampleByPercentileContext ctx);
	/**
	 * Exit a parse tree produced by the {@code sampleByPercentile}
	 * labeled alternative in {@link DorisParser#sampleMethod}.
	 * @param ctx the parse tree
	 */
	void exitSampleByPercentile(DorisParser.SampleByPercentileContext ctx);
	/**
	 * Enter a parse tree produced by the {@code sampleByRows}
	 * labeled alternative in {@link DorisParser#sampleMethod}.
	 * @param ctx the parse tree
	 */
	void enterSampleByRows(DorisParser.SampleByRowsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code sampleByRows}
	 * labeled alternative in {@link DorisParser#sampleMethod}.
	 * @param ctx the parse tree
	 */
	void exitSampleByRows(DorisParser.SampleByRowsContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#errorCapturingIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterErrorCapturingIdentifier(DorisParser.ErrorCapturingIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#errorCapturingIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitErrorCapturingIdentifier(DorisParser.ErrorCapturingIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code errorIdent}
	 * labeled alternative in {@link DorisParser#errorCapturingIdentifierExtra}.
	 * @param ctx the parse tree
	 */
	void enterErrorIdent(DorisParser.ErrorIdentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code errorIdent}
	 * labeled alternative in {@link DorisParser#errorCapturingIdentifierExtra}.
	 * @param ctx the parse tree
	 */
	void exitErrorIdent(DorisParser.ErrorIdentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code realIdent}
	 * labeled alternative in {@link DorisParser#errorCapturingIdentifierExtra}.
	 * @param ctx the parse tree
	 */
	void enterRealIdent(DorisParser.RealIdentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code realIdent}
	 * labeled alternative in {@link DorisParser#errorCapturingIdentifierExtra}.
	 * @param ctx the parse tree
	 */
	void exitRealIdent(DorisParser.RealIdentContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(DorisParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(DorisParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unquotedIdentifier}
	 * labeled alternative in {@link DorisParser#strictIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterUnquotedIdentifier(DorisParser.UnquotedIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unquotedIdentifier}
	 * labeled alternative in {@link DorisParser#strictIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitUnquotedIdentifier(DorisParser.UnquotedIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code quotedIdentifierAlternative}
	 * labeled alternative in {@link DorisParser#strictIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterQuotedIdentifierAlternative(DorisParser.QuotedIdentifierAlternativeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code quotedIdentifierAlternative}
	 * labeled alternative in {@link DorisParser#strictIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitQuotedIdentifierAlternative(DorisParser.QuotedIdentifierAlternativeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#quotedIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterQuotedIdentifier(DorisParser.QuotedIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#quotedIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitQuotedIdentifier(DorisParser.QuotedIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code integerLiteral}
	 * labeled alternative in {@link DorisParser#number}.
	 * @param ctx the parse tree
	 */
	void enterIntegerLiteral(DorisParser.IntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code integerLiteral}
	 * labeled alternative in {@link DorisParser#number}.
	 * @param ctx the parse tree
	 */
	void exitIntegerLiteral(DorisParser.IntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code decimalLiteral}
	 * labeled alternative in {@link DorisParser#number}.
	 * @param ctx the parse tree
	 */
	void enterDecimalLiteral(DorisParser.DecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code decimalLiteral}
	 * labeled alternative in {@link DorisParser#number}.
	 * @param ctx the parse tree
	 */
	void exitDecimalLiteral(DorisParser.DecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link DorisParser#nonReserved}.
	 * @param ctx the parse tree
	 */
	void enterNonReserved(DorisParser.NonReservedContext ctx);
	/**
	 * Exit a parse tree produced by {@link DorisParser#nonReserved}.
	 * @param ctx the parse tree
	 */
	void exitNonReserved(DorisParser.NonReservedContext ctx);
}