-- hsqldb get aggregated data from LOCALIZED
select
    a.PACKAGE_NAME,
    GROUP_CONCAT(l.NAME order by c.CODE SEPARATOR ' | ') as NAME,
    GROUP_CONCAT(l.SUMMARY ORDER BY c.CODE SEPARATOR ' | ') as SUMMARY,
    GROUP_CONCAT(l.WHATS_NEW ORDER BY c.CODE SEPARATOR ' | ') as WHATS_NEW,
    GROUP_CONCAT(c.CODE ORDER BY c.CODE SEPARATOR ' | ') as language
from app a
inner join LOCALIZED l on l.APP_ID = a.id
inner join Locale c on l.LOCALE_ID = c.id
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
inner join Locale la on lo.LOCALE_ID = la.id
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

----

-- jdbc:hsqldb:file:/home/EVE/.fdroid/db/fdroid
-- jdbc:hsqldb:hsql://localhost/fdroid

-- java -classpath ~/Downloads/hsqldb/lib/hsqldb.jar org.hsqldb.server.Server --database.0 file:evers-db-create --dbname.0 evers-db-create
-- java -classpath ~/Downloads/hsqldb/lib/hsqldb.jar org.hsqldb.server.Server --database.0 file:fdroid --dbname.0 fdroid


-----------------------------------

-- hide all localized that are not de en es nl fr

update Locale set languagePriority = 9 where code = 'de';
update Locale set languagePriority = 8 where code = 'en';
update Locale set languagePriority = 7 where code = 'es';
update Locale set languagePriority = 6 where code = 'nl';
update Locale set languagePriority = 5 where code = 'fr';
update Locale set languagePriority = -1 where languagePriority = 0;

insert into HardwareProfile(id,name,sdkVersion,nativecode) values(3,'android-10', 29, 'armeabi-v7a,armeabi');
insert into HardwareProfile(id,name,sdkVersion,nativecode) values(2,'android-7.0', 24, 'arme64-v8a,armeabi-v7a,armeabi');
insert into HardwareProfile(id,name,sdkVersion,nativecode) values(1, 'android-4.2', 17, 'armeabi-v7a');
