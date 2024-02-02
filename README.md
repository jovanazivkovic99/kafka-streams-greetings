# Greetings Kafka Streams Aplikacija

Ovaj dokument opisuje arhitekturu i tok Greetings Kafka Streams aplikacije, koja se sastoji od dve primarne klase: `GreetingsTopology` i `GreetingsStreamApp`.

## Pregled

Aplikacija demonstrira jednostavan Kafka Streams procesni tok, transformišući poruke iz ulaznog topika u format velikih slova i objavljujući ih na izlazni topik.

### GreetingsTopology

Klasa `GreetingsTopology` odgovorna je za definisanje topologije obrade streamova. Topologija u Kafka Streams je u suštini graf procesora streamova (ili čvorova) koji obrađuju zapise iz ulaznih streamova.

#### Ključne karakteristike:

- **Ulazni Topik**: Klasa koristi `"greetings"` kao ulazni topik iz kojeg se konzumiraju poruke.
- **Izlazni Topik**: Specifikuje `"greetings_uppercase"` kao destinacijski topik za objavljivanje transformisanih poruka.
- **Transformacija**: Implementira korak transformacije koji poruke pretvara iz malih u velika slova.

#### Metoda buildTopology:

Ova metoda konstruiše topologiju obrade koristeći `StreamsBuilder`. Definiše jednostavan tok:

1. **Kreiranje Ulaznog Streama**: Inicira stream iz topika `greetings`.
2. **Transformacija Poruka**: Koristi metodu `mapValues` za transformaciju svake vrednosti poruke u velika slova.
3. **Objavljivanje Transformisanih Poruka**: Transformisane poruke se zatim objavljuju na topik `greetings_uppercase`.

### GreetingsStreamApp

Glavna klasa aplikacije, `GreetingsStreamApp`, odgovorna je za konfigurisanje, inicijalizaciju i pokretanje Kafka Streams aplikacije.

#### Konfiguracija i Postavljanje:

- **Konfiguracija Kafka Streams**: Uključuje postavljanje ID-a aplikacije i informacija o Kafka klasteru.
- **Kreiranje Topika**: Metoda `createTopics` pokušava da kreira potrebne Kafka topike ako oni ne postoje.

#### Inicijalizacija i Izvršavanje:

- **Izgradnja Topologije**: Koristi `GreetingsTopology` za izgradnju topologije obrade streamova.
- **Pokretanje Aplikacije**: Inicijalizuje instancu `KafkaStreams` sa definisanom topologijom i konfiguracijama, a zatim pokreće aplikaciju.

#### Graciozno Gašenje:

- Implementira hook za gašenje kako bi se osiguralo da aplikacija graciozno zatvori i oslobodi resurse prilikom gašenja.

### Tok Aplikacije

1. **Konfiguracija**: Metoda `main` konfiguriše Kafka Streams aplikaciju i kreira potrebne topike.
2. **Konstrukcija Topologije**: Izgrađuje topologiju obrade streamova koristeći klasu `GreetingsTopology`.
3. **Obrada Streamova**: Pokreće aplikaciju, konzumira poruke iz topika `greetings`, transformiše ih u velika slova i objavljuje transformisane poruke na topik `greetings_uppercase`.

## Zaključak

Greetings Kafka Streams Aplikacija ilustruje osnovni slučaj upotrebe Kafka Streams za transformaciju i obradu poruka. Demonstrira kako postaviti jednostavan pipeline obrade streamova, od konzumiranja poruka iz izvornog topika, transformacije sadržaja poruka, do objavljivanja na destinacijski topik.
