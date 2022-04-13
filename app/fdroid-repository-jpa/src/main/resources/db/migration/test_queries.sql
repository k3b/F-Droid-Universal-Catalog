-- hsqldb get aggregated data from LOCALIZED
select
    a.PACKAGE_NAME,
    GROUP_CONCAT(l.NAME order by c.CODE SEPARATOR ' | ') as NAME,
    GROUP_CONCAT(l.SUMMARY ORDER BY c.CODE SEPARATOR ' | ') as SUMMARY,
    GROUP_CONCAT(l.WHATS_NEW ORDER BY c.CODE SEPARATOR ' | ') as WHATS_NEW,
    GROUP_CONCAT(c.CODE ORDER BY c.CODE SEPARATOR ' | ') as language
from app a
inner join LOCALIZED l on l.APP_ID = a.id
inner join LOCALE c on l.LOCALE_ID = c.id
where a.PACKAGE_NAME like '%.k3b.%'
GROUP by a.PACKAGE_NAME;

------
-- hsqldb find package_name in main fields orderd by relevance-score
select
    id,
    PACKAGE_NAME,
    sum(score) score
from APP_SEARCH
where search like '%k3b%'
group by id, PACKAGE_NAME
order by sum(score) desc, PACKAGE_NAME;

-------
-- hsqldb find in main fields orderd by relevance-score
select * from app where id in (
    select
        id,
        PACKAGE_NAME,
        sum(score) score
    from APP_SEARCH
    where search like '%k3b%'
    group by id, PACKAGE_NAME
    order by sum(score) desc, PACKAGE_NAME;
)

-----
-- hsqldb find orderd by oldest-supported-sdk
select
    PACKAGE_NAME,
    SEARCH_SDK,
    SEARCH_VERSION
from app
where PACKAGE_NAME like '%k3b%'
order by SEARCH_SDK, PACKAGE_NAME
