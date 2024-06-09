## PPJ project METEO

Cílem projektu je vytvořit aplikaci pro ukládání a zobrazování meteorologických dat.   

**Požadavky na technické řešení**  
    1. Maven pro sestavení  
    2. Spring (Boot) jako implementační framework  
    3. Verzování na GitHubu  
    
**Datový model (persistence)**  
    • Stát (MySQL)  
    • Město (MySQL)  
    • Měření pro město (MySQL)  
    
**API**  
Aplikace bude poskytovat REST API pro přímou komunikaci.  

**REST**  
Aplikace bude obsahovat REST rozhraní pro přidávání, editaci a mazání států, měst a měření. A dále zobrazení aktuálních hodnot a průměru za poslední den, týden a 14 dní.  

**Testování**  
Součástí řešení budou testy pro všechny operace volané přes REST API. 

**Konfigurace**  
Musí být možno provádět externí konfiguraci – tj. veškerá konfigurace do properties souborů.   

**Logování**  
Aplikace by měla využívat logovací systém Logback s výpisem do souboru (např. log.out). V případě chyby Vám bude zaslán pouze soubor log.out – výstup z konzole pouze v případě, že neprojdou testy.  

**Data**  
Data je možné získávat z libovolného veřejně dostupného API, například http://www.openweathermap.com  - s bezplatným přístupem při dodržení limitu 60 volání za sekundu.   
