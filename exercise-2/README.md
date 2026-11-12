# Esercizio 2 register.it

Moltiplica numeri interi usando solo l'addizione tra singole cifre [0,9]

Requisiti: **JDK 21** o superiore ([download Temurin 21](https://adoptium.net/temurin/releases?version=21&os=any&arch=any)). Maven non serve: il wrapper è incluso nel progetto

## Comandi

Dalla cartella `exercise-2/`, per compilare, eseguire i test e creare il jar:

```bash
./mvnw verify
```
o
```bat
.\mvnw.cmd verify
```

Per eseguire:

```bash
java -jar target/multiplication.jar 15 2     # stampa 30
java -jar target/multiplication.jar 15 2 3   # stampa 90
java -jar target/multiplication.jar          # stampa 1
```

## Algoritmo

I numeri sono array di cifre, `15` è `[5, 1]`.

La moltiplicazione è quella in colonna, ma ogni riga si ottiene con addizioni ripetute invece che moltiplicazini. Ci sono sostanzialmente 2 metodi:

- `shift`: serve per aggiungere gli zeri quando si moltiplica ad esempio per le decine
- `add`: fa le addizioni in colonna


## Possibili miglioramenti

- Supporto ai numeri negativi
- Più modalità di input
