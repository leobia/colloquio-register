# Esercizio 1 register.it


Genera un report conteggiando le righe per indirizzo ip dal file di log `logfiles/requests.log`. Per le specifiche dettagliate rifarsi al pdf.

Requisiti: **JDK 25**. Per maven si trovano i wrapper nel progetto: `mvnw` o `mvnw.cmd` (gli esempi sotto usano mvnw ma se si é su windows sostituirlo con mvnw.cmd)

## Comandi

### Build e test
```bash
./mvnw verify
```

### Run

```bash
java -jar target/traffic-report.jar                      # tutti i giorni, output CSV
java -jar target/traffic-report.jar --date 2026-11-10    # solo il giorno indicato
java -jar target/traffic-report.jar --format json        # output JSON
java -jar target/traffic-report.jar --help
```

## Formato di input

Nel file `logfiles/requests.log` si trovano le righe delle richieste da analizzare

Esempio di riga:

```
2026-11-10T08:15:30;1024;OK;192.168.0.1
```

## Assunzioni

- Ogni riga di `logfiles/requests.log` contiene esattamente 4 campi separati da `;`
- `TIMESTAMP` é una data ISO-8601 senza offset e gestione dei fusi orari per semplicitá `2026-11-10T08:15:30`
- `BYTES` é intero non negativo 
- `STATUS` é preso da RFC 2616 ed é tipo `OK`, `Not Found`
- `REMOTE_ADDR` per semplicitá nella validazione controllo solamente che non abbia spazi nel mezzo
- **Spazi**: ogni campo viene ripulito dagli spazi iniziali e finali prima della validazione
- **Ordinamento**: a parità di richieste vince byte decrescenti; a parità anche di byte, per IP in ordine alfabetico
- **Output**: `reports/ipaddr.csv` oppure `reports/ipaddr.json`. La cartella `reports/` viene creata se manca e se il report esiste giá viene sovrascritto


## Strumenti e dipendenze

L'esercizio chiede di non usare framework ma solo Java, ho deciso di utilizzare Maven perché comunque molto utile per strumento di build e la dipendenza junit perché anche questa molto utile nei test

## Possibili miglioramenti

- Percorsi di input e output configurabili
- Evitare sovrascritture di report vecchi
- Validazione stretta degli indirizzi IPv4/IPv6
- Timestamp con fuso orario
- Test di carico e di performance
- Validazione più ricca, che riporti nel warning il motivo dello scarto (campo mancante, timestamp non valido, byte negativi, ...)
- Più casi di test per il parser: riga vuota o di soli spazi e equivalente per i singoli campi, byte oltre il limite di `long`
- Percentuali calcolate con il metodo dei resti maggiori, così che ogni colonna sommi esattamente `100.00` (ora l'arrotondamento `HALF_UP` può dare `99.99` o `100.01`)
- Campi CSV tra virgolette secondo RFC 4180, nel caso un valore contenga `,` o `"`
