package com.example.proy2

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AdministradorBD(context: Context) :
    SQLiteOpenHelper(
        context,
        "Universidad.db",
        null,
        1
    ) {

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(
            """
            CREATE TABLE Estudiantes (
                id          INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre      TEXT    NOT NULL,
                carrera     TEXT    NOT NULL,
                grupo       TEXT    NOT NULL,
                notificaciones INTEGER NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE Calificaciones (
                id           INTEGER PRIMARY KEY AUTOINCREMENT,
                estudiante_id INTEGER NOT NULL,
                asignatura   TEXT    NOT NULL,
                nota1        REAL    NOT NULL,
                nota2        REAL    NOT NULL,
                nota3        REAL    NOT NULL,
                nota4        REAL    NOT NULL,
                promedio     REAL    NOT NULL,
                condicion    TEXT    NOT NULL,
                fecha        TEXT    NOT NULL,
                FOREIGN KEY(estudiante_id) REFERENCES Estudiantes(id)
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS Calificaciones")
        db.execSQL("DROP TABLE IF EXISTS Estudiantes")
        onCreate(db)
    }
}