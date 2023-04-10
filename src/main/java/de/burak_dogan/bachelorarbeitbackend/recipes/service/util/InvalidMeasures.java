package de.burak_dogan.bachelorarbeitbackend.recipes.service.util;

/**
 * Diese Aufzaehlung enthaelt ungueltige Masseinheiten fuer das Importieren der Rezepte.
 * Rezepte, die Zutaten mit diesen Masseinheiten enthalten, werden nicht gespeichert.
 */
public enum InvalidMeasures {
    TEASPOON,
    JAR
}