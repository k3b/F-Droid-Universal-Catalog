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
order by SEARCH_SDK, PACKAGE_NAME;

----
--- number of existing translations
select l.code, l.NAME_ENGLISH, count(*)
from LOCALIZED lo
inner join Locale l on lo.LOCALE_ID = l.id
group by l.code, l.NAME_ENGLISH
order by count(*) desc
;

-----
-- number of apps orderd by minSdk
select count(*) cnt, substr(SEARCH_SDK,2,2) minSdk
from app
group by substr(SEARCH_SDK,2,2)
order by substr(SEARCH_SDK,2,2);

-----
-- apps description in one language
select a.PACKAGE_NAME, la.code, lo.NAME, lo.SUMMARY, lo.DESCRIPTION
from LOCALIZED lo
inner join LOCALE la on lo.LOCALE_ID = la.id
inner join App a on lo.APP_ID = a.id
where la.code = 'de' and lo.DESCRIPTION is not null
;


-----------------------------------

-- apps with maxsdk
select APP_ID, APK_NAME, VERSION_CODE, VERSION_NAME, MIN_SDK_VERSION, MAX_SDK_VERSION, TARGET_SDK_VERSION, NATIVECODE from APP_VERSION where MAX_SDK_VERSION <> 0

select
    av.APP_ID, a.PACKAGE_NAME, av.VERSION_CODE, av.VERSION_NAME, av.MIN_SDK_VERSION,
    av.MAX_SDK_VERSION, av.TARGET_SDK_VERSION, av.NATIVECODE
from APP_VERSION av
inner join APP a on av.APP_ID = a.id
where av.APP_ID in (
    select j.APP_ID
        from APP_VERSION j
        where j.MAX_SDK_VERSION<> 0
        group by j.APP_ID
        having count(*) > 2)
order by a.PACKAGE_NAME, av.VERSION_CODE desc;

    select APP_ID, count(*) as cnt
    from APP_VERSION
    where MAX_SDK_VERSION<> 0
    group by APP_ID
    having count(*) > 2
    order by cnt desc;

--
select
    av.APP_ID, a.PACKAGE_NAME, av.VERSION_CODE, av.VERSION_NAME, av.MIN_SDK_VERSION,
    av.MAX_SDK_VERSION, av.TARGET_SDK_VERSION, av.NATIVECODE
from APP_VERSION av
inner join APP a on av.APP_ID = a.id
where av.APP_ID in (12769,46084,468,31082,53984)
order by a.PACKAGE_NAME, av.VERSION_CODE desc;

-----------------------------------

-- hide all localized that are not de en es nl fr
update LOCALE set LANGUAGE_PRIORITY = 9 where code = 'de';
update LOCALE set LANGUAGE_PRIORITY = 8 where code = 'en';
update LOCALE set LANGUAGE_PRIORITY = 7 where code = 'es';
update LOCALE set LANGUAGE_PRIORITY = 6 where code = 'nl';
update LOCALE set LANGUAGE_PRIORITY = 5 where code = 'fr';
update LOCALE set LANGUAGE_PRIORITY = -1 where LANGUAGE_PRIORITY = 0;
