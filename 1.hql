select * from
         (
         select * from dbo
         ) AS tmp1
left join
         (
         select * from da.dbp
         ) AS tmp2
on tmp1.id = tmp2.id