/* select * from realproperty p where 
apnClean in 
( select apnClean from realproperty group by apnClean having count(apnClean)>1 ); */

/* select * from propertyTransaction p where property_id != null;*/
/* select count(*) from property_transaction p where realProperty_id is not null; */
/* drop table property_pransaction; */
/* select count(*) from property_transaction p where state = "CA";*/

/*
select apn,propertyAddress, addressClean from real_property p where 
addressClean in 
( select addressClean from real_property group by addressClean having count(addressClean)>1 );
*/
/*
select count(id) as totalCnt, count(realProperty_id) as linkedCnt, 
100*count(realProperty_id)/count(id) as linkedPercent, substring(zip,1,5) as zipCode, city 
from property_transaction where substring(zip,1,5) in ('94610', '94611', '94618') 
group by zipCode, city
having count(id) > 3;
*/

/*
select count(*) from property_transaction;
select count(*) from real_property;*/

/*
drop table agent;
*/
/*
select status,  
count(ListingAgentName), count(SellingAgent1Name), count(CoListAgentName), count(CoSellAgentName), 
count(ListingAgentLicenseID), count(SellingAgent1LicenseId), count(CoListAgentLicenseId), count(CoSellAgentLicenseId),
count(ListAgentBreNum), count(SoldAgentBreNum), count(CoListAgentBreNum), count(CoSellAgentBreNum),
count(listingAgent_id), count(sellingAgent_id), count(listingAgent2_id), count(sellingAgent2_id)
from property_transaction group by status;
*/
/*
select substr(zip,1,5) as zipCode,
count(ListingAgentName) as list1NameCnt,
100 * count(listingAgent_id) / count(ListingAgentName) as list1Percent,
count(SellingAgent1Name) as sell1NameCnt,
100 * count(sellingAgent_id) / count(SellingAgent1Name) as sell1Percent,
count(CoListAgentName) as list2NameCnt,
100 * count(listingAgent2_id) / count(CoListAgentName) as list2Percent,
count(CoSellAgentName) as sell2NameCnt,
100 * count(sellingAgent2_id) / count(CoSellAgentName) as sell2Percent
from property_transaction 
where substr(status,1,3) = 'SLD' and substr(zip,1,5) in ('94610','94611','94618')
group by substr(zip,1,5);
*/
/*
select buildingType, status, 
count(realProperty_id), count(id), (count(id) - count(realProperty_id))/count(id)
from property_transaction
where zip like "94611%"
group by  buildingType, status;
*/


/*select buildingType, status, landUse, substr(zip,1,5) as zipCode,
count(realProperty_id), count(pt.id) 
from property_transaction pt, real_property rp
where pt.realProperty_id = rp.id and substr(status,1,3) = 'SLD' 
group by  buildingType, status, landUse, zipCode;*/

/*
select count(id), substr(propertyZip,1,5), propertyCity, landUse
from real_property
where landUse in ('Single Family Residential', 'Residential Condominium', 'Residential Townhouse') 
group by substr(propertyZip,1,5), propertyCity, landUse;

select count(pt.id), substr(zip,1,5),  city, buildingType, year(closeDate) 
from property_transaction pt, real_property rp
where pt.realProperty_id = rp.id and substr(status,1,3) = 'SLD' and substr(zip,1,5) in ('94610','94611', '94618') 
group by  substr(zip,1,5),  city, buildingType, buildingType, year(closeDate);
*/

/*
select 
count(id)
from real_property
where propertyZip like "94611%";
*/
 
/*
where lastSaleDate is not null and propertyZip = '94611';
*/
/*
between CAST('2014-01-01' AS DATE) and cast('2015-01-01' as date);
*/

/*
select count(*) from agent;
*/


/*
--select ListingAgentName, ListingAgentLicenseID, ListAgentBreNum,
-- SellingAgent1Name, SellingAgent1LicenseId,SoldAgentBreNum,
--CoListAgentLicenseId, CoSellAgentLicenseId, CoListAgentBreNum, 
--CoListAgentName, CoSellAgentName, CoSellAgentBreNum
--from property_transaction;
*/
/*
select lower(listingAgentName), listingAgentLicenseID from property_transaction
where listingAgentLicenseId is null and lower(listingAgentName) not in (
select lower(listingAgentName) from property_transaction
where listingAgentLicenseID is not null and listingAgentName is not null
);
*/
/*
select lower(listingAgentName) from property_transaction where
listingAgentName is not null;
*/
/*
ALTER TABLE agent DROP index UK8e2w9ma3jkny0yhpsrltoe0qw;
*/

/*
 ALTER TABLE agent MODIFY COLUMN lastName varchar(255) NULL;
 *

/*
select concat(lower(listingAgentName), listingAgentLicenseID) from property_transaction
group by concat(lower(listingAgentName), listingAgentLicenseID)
having count(concat(lower(listingAgentName), listingAgentLicenseID)) > 1;
*/

/*
select status, count(id) from property_transaction group by status;
*/
/*
select count(*) from property_transaction;
*/
/*
drop table real_property;
drop table agent;
drop table agent_volume;
drop table property_transaction;
*/



/*
select lastSaleDate from real_property where lastSaleDate is not null;
*/
/*
select year(closeDate) as closeYear, city, substring(zip,1,5) as zipCode, count(id) 
from property_transaction
where closeDate is not null
group by closeYear, city, zipCode;
*/

/*
select sum(case when(pt.listingAgent_id = pt.sellingAgent_id) then 1 else 0 end) as doubleEnded, 
(100*sum(case when(pt.listingAgent_id = pt.sellingAgent_id) then 1 else 0 end)/count(pt.id)) as percentDoubleEnded, count(pt.id) as allTrans, a.agentName 
from property_transaction pt, agent a
where pt.sellingAgent_id = a.id or pt.listingAgent_id = a.id 
group by a.agentName
having count(pt.id) > 20
order by percentDoubleEnded desc;
*/



/*select count(*) from property_transaction;
*/

/*delete from property_transaction;*/

/*
* Relative sales activity by year
*/

/*
* Houses on the market by week
*/
/*
select count(*) as cnt, substr(1,5,zip), city, buildingType from property_transaction 
where (offMarketDate is not null and '1997-01-05' between listDate and date_add(offMarketDate,interval 1 day))
or ((offMarketDate is null) and substr(status,1,3) = "ACT")
group by substr(1,5,zip), city, buildingType;

select count(*) as cnt, substr(z1,5,zip), city, buildingType from property_transaction 
where (offMarketDate is not null and '1997-01-05' between listDate and date_add(offMarketDate,interval 1 day)) 
or ((offMarketDate is null) and substr(status,1,3) = 'ACT') 
group by substr(1,5,zip), city, buildingType; 
*/

/*
select count(*) as cnt, pt.zip5 as MLSzip, pt.area as MLSarea, pt.city as MLScity, rp.propertyZip5 as CRSZip, rp.propertyCity as CRSCity from property_transaction pt, real_property rp
where pt.realProperty_id = rp.id
group by pt.zip5, pt.area, pt.city, rp.propertyZip5, rp.propertyCity; 
*/
/*
select mlsNo from property_transaction pt 
where listingAgent_id = 125823 or listingAgent2_id = 125823 
or sellingAgent_id = 125823 or sellingAgent2_id = 125823
order by mlsNo asc
*/
/*
select count(*) from property_transaction;
SHOW GLOBAL STATUS LIKE 'created_tmp%tables';
*/
/*
update real_property set propertyAdr_id = null;

delete from address;
*/
/*
select count(*) from real_property where propertyAdr_id is null;
select count(*) from real_property;
*/
/* check for duplicate addresses */

/*
select count(*) from address;
select count(location) from address;
select count(mapLocCalls) from address where mapLocCalls != 0;
*/

/*
UPDATE agent SET agentName = ${agentName||Callahan, Kathleen M||String||nullable ds=255 dt=VARCHAR}$ WHERE id = ${id (where)||121534||Long||where pk unformatted ds=20 dt=BIGINT}$
*/

select distinct tourDate from tourlist_entry order by tourDate desc;
delete from tourlist_entry;