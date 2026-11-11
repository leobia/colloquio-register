# Esercizio 1 register.it

Genera un report del traffico per indirizzo IP (richieste, byte e relative percentuali) a partire da `logfiles/requests.log`, considerando solo le righe con status `OK`.

Requisiti: **JDK 21** o superiore ([download Temurin 21](https://adoptium.net/temurin/releases?version=21&os=any&arch=any)). Maven non serve: il wrapper è incluso nel progetto.

## Comandi

Dalla cartella `exercise-1/`, per compilare, eseguire i test e creare il jar:

```bash
./mvnw verify
```
o
```bat
.\mvnw.cmd verify
```

Per generare il report:

```bash
java -jar target/traffic-report.jar                                  # tutti i giorni, CSV
java -jar target/traffic-report.jar --date 2026-11-10 --format json  # un solo giorno, JSON
```

Opzioni: `--date yyyy-MM-dd` (default: tutti i giorni) e `--format csv|json` (default: `csv`). Il report viene scritto in `reports/ipaddr.csv` o `reports/ipaddr.json`.

## Scelte principali

- Per semplicitá i timestamp sono ISO-8601 senza offset
- Le righe in input malformate (piú di 4 campi, timestamp non ISO-8601 o con fuso orario, byte negativi o non numerici, IP vuoto o con spazi interni) vengono scartate
- Entrano nel report solo le righe con `STATUS` = `OK`
- L'ordine é per request decrescente e i tie breaker sono: più byte e poi l'IP in ordine alfabetico
- Le percentuali sono calcolate sui soli record inclusi, con 2 decimali `HALF_UP` quindi la somma puó anche fare `99.99` o `100.01`
- Senza `--date` specificato l'output é su tutte le date 
- CSV con separatore `,` e fine riga `\r\n`; JSON su una sola riga
- Se il report in output esiste giá viene sovrascritto

## Struttura del codice

Package `it.leobia.register.exercise`:

- `parser.LogParser`: valida una riga del log e la converte in `model.LogEntry`
- `service.ReportGenerator`: legge, filtra, aggrega per IP, ordina e scrive il report (`model.ReportRow`)
- `writer`: `CsvReportWriter` e `JsonReportWriter`, scelti tramite `OutputFormat`

## Dipendenze

Maven serve solo per la build e JUnit è usato solo nei test

## Possibili miglioramenti

- Percorsi di input e output configurabili, opzione `--help` ed exit code distinti
- Scrittura di un file temporaneo e poi rename e nessuna sovrascrittura dei report precedenti
- Validazione stretta di IPv4/IPv6 e timestamp con fuso orario
- Motivo dello scarto nel warning delle righe malformate
- Migliorare le percentuali, perché ogni colonna sommi sempre `100.00`
- Quoting dei campi CSV (RFC 4180) ed escape delle stringhe JSON
- Più test: ordinamento a parità di richieste e byte, carico e performance
