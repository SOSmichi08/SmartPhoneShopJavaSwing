# Smartphone Shop Admin

## Project Overview

This repository contains the implementation of the **Smartphone Shop Admin** application, developed as part of **Module 165**. The application is designed to manage smartphones, customers, and orders for an online shop. It provides an administrative interface to handle product inventory and customer orders using Java and MongoDB.

## Features

- **Smartphone Management**: Add, update, delete, and list available smartphones.
- **Customer Management**: Store and manage customer details.
- **Order Management**: Process and track customer orders, including multiple items per order.
- **Persistence**: Uses MongoDB as a NoSQL database for data storage.
- **User Interface**: Implemented using either a console-based UI or Java Swing.

## Technologies Used

- **Programming Language**: Java
- **Development Environment**: IntelliJ IDEA
- **Database**: MongoDB (NoSQL)
- **Version Control**: Git & GitHub
- **UI Options**: Console Application or Java Swing

## Data Models

### Smartphone
```plaintext
- Marke
- Modell
- Einzelpreis
- Arbeitsspeicher (4GB, 8GB)
- Bildschirmgröße (6")
- Speicherkapazität (128GB, 256GB, 512GB)
- Betriebssystem (iOS, Android)
- Betriebssystem-Version (iOS11, Android 13)
- Pixelauflösung (2340x1080)
- Anzahl Prozessorkerne (8-Core)
- Akkukapazität (3900mAh)
- Konnektivität (Bluetooth, NFC, USB, WiFi Hotspot, WLAN)
- Mobilfunkstandard
```
### Customer
```plaintext
- Anrede
- Nachname
- Vorname
- Adresse (Straße, PLZ, Ort)
- Telefon (Privat, Mobil)
- E-Mail
- Geburtsdatum
- Benutzername
- Passwort
```
### Order
``` plaintext
- Bestellnummer
- Bestelldatum
- Kunde (Referenz)
- Bestellpositionen (Smartphone, Einzelpreis, Stückzahl)
- Gesamtbetrag
```
### Installation & Setup
<ol>Clone the Repository</ol>

```bash
git clone https://github.com/your-username/smartphone-shop-admin.git
cd smartphone-shop-admin
```
<ul>Install MongoDB and start the service</ul>
<ul>Setup MongoDB</ul>
<ul>Open in IntelliJ IDEA.</ul>
<ul>Configure the database connection in the application</ul>
<ul>Build and run the Project</ul>
