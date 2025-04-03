## Cupcake project for SEM2

- Jonathan Kudsk (cph-jk513@cphbusiness.dk)
- Jonas Outzen (cph-jo221@cphbusiness.dk)
- Marcus Forsberg (cph-mf411@cphbusiness.dk)

## Baggrund
Vi har landet en vigtig opgave fra Olsker Cupcakes. Det er endnu et dybdeøkologisk iværksættereventyr fra Bornholm, som har ramt den helt rigtige opskrift. Et par hipstere fra København har været forbi bageriet, og indsamlet nogle krav og lavet en halvfærdig mockup af en tænkt forside. En mockup er en meget løs skitse, som viser hvordan det færdige website skal se ud. Det er selvfølgelig ikke alt som er med, og som er tænkt igennem, så det er vores opgave at stille spørgsmål til manglende funktionalitet, komme med forslag osv.

Når man som leverandør skal løse sådan en opgave, er det godt at dele opgaven op i små etaper. Så tænk fra starten over hvor lidt vi kan lave for at få den første prototype i luften. Med andre ord: vi skal ikke lade os forblænde af hipsternes farver og striber og mange funktionaliteter. Vi æder elefanten lidt ad gangen.

## User stories (funktionelle krav)
Det første kundemøde mundede ud i en række såkaldte user-stories. De beskriver på kort form hvilke brugere, som har hvilke behov og hvad de ønsker at opnå. Det kan godt være at der dukker flere user-stories op undervejs i processen, eller I vælger at stryge nogle af dem:

- US-1: Som kunde kan jeg bestille og betale cupcakes med en valgfri bund og top, sådan at jeg senere kan køre forbi butikken i Olsker og hente min ordre.

- US-2 Som kunde kan jeg oprette en konto/profil for at kunne betale og gemme en en ordre.

- US-3: Som administrator kan jeg indsætte beløb på en kundes konto direkte i Postgres, så en kunde kan betale for sine ordrer.

- US-4: Som kunde kan jeg se mine valgte ordrelinier i en indkøbskurv, så jeg kan se den samlede pris.

- US-5: Som kunde eller administrator kan jeg logge på systemet med email og kodeord. Når jeg er logget på, skal jeg kunne se min email på hver side (evt. i topmenuen, som vist på mockup’en).

- US-6: Som administrator kan jeg se alle ordrer i systemet, så jeg kan se hvad der er blevet bestilt.

- US-7: Som administrator kan jeg se alle kunder i systemet og deres ordrer, sådan at jeg kan følge op på ordrer og holde styr på mine kunder.

- US-8: Som kunde kan jeg fjerne en ordrelinie fra min indkøbskurv, så jeg kan justere min ordre.

- US-9: Som administrator kan jeg fjerne en ordre, så systemet ikke kommer til at indeholde udgyldige ordrer. F.eks. hvis kunden aldrig har betalt.

## Ikke-funktionelle krav

### Design og brugeroplevelse
- Der udarbejdes en mockup i Figma eller et lignende værktøj, som viser de websider, den færdige løsning vil bestå af.
- Websitet skal være responsivt og fungere både på en almindelig skærm og en mobiltelefon (iPhone 12 eller lignende). Hvis det giver udfordringer, fokuseres der primært på en laptop-version.

### Database
- Ordrer, kunder og øvrige data gemmes i en Postgres-database.
- Databasen skal normaliseres til tredje normalform, medmindre der er en god grund til at afvige herfra.

### Teknologi og udvikling
- Kildekoden skal deles på GitHub.
- Udviklingen skal ske med følgende teknologier:
  - Java 17
  - Javalin
  - Thymeleaf template engine
  - Postgres Database
  - HTML og CSS

## Leverancer

### Projektets output
- Et ER-diagram (ERD) over databasen.
- Et funktionelt website, der dækker Olsker Cupcakes’ behov.
- Websitet hostes ikke online, men skal kunne fremvises lokalt på en laptop.

### Arbejdsproces
- Hver user story implementeres én ad gangen.
- Kunden forventer som minimum en løsning på de første seks user stories.
- Gruppen prioriterer selv rækkefølgen af user stories og estimerer tidsforbruget.
- Efter implementering af de første seks user stories fortsættes arbejdet med resten, én ad gangen, indtil deadline.

### Dokumentation og præsentation
- Der udarbejdes en kort rapport samt anden relevant teknisk dokumentation.
- Webshoppen bør være færdigudviklet i løbet af mandag i projektets uge 2, så de sidste dage kan bruges på dokumentation.
- Rapporten skal indeholde et link til en kort video-demo af den færdige løsning (2-3 minutter). Videoen optages med et screencast-værktøj som ScreenPal, Zoom eller Panopto og uploades til YouTube som et skjult link eller lignende.

## Aflevering
- Projektet udføres i grupper af 2-4 personer.
- Gruppetilmelding foretages på Moodle.
- Fuld deltagelse i projektet giver 30 study points. Det kræver aktiv medvirken i gruppens arbejde, levering af en løsning og deltagelse i prøveeksamen.
- Opgaven afleveres via Moodle med et link til GitHub, hvor kode og rapport er tilgængelig.
- Rapporten skal være i PDF-format og placeres i en separat mappe ved navn “rapport”. Filnavnet skal være “rapport.pdf”.

### Eksamen
- Yderligere information om prøveeksamen følger senere.
