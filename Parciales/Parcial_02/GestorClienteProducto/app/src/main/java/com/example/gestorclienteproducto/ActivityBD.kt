package com.example.gestorclienteproducto

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ActivityBD (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "GestorOrdenes.db"
        const val DATABASE_VERSION = 1

        // Tabla Clientes
        const val TABLE_CLIENTES = "Clientes"
        const val COL_ID_CLIENTE = "idCliente"
        const val COL_NOMBRE = "nombre"
        const val COL_CORREO = "correo"
        const val COL_TELEFONO = "telefono"

        // Tabla Productos
        const val TABLE_PRODUCTOS = "Productos"
        const val COL_ID_PRODUCTO = "idProducto"
        const val COL_NOMBRE_PRODUCTO = "nombreProducto"
        const val COL_PRECIO = "precio"
        const val COL_DESCRIPCION = "descripcion"

        // Tabla Ordenes
        const val TABLE_ORDENES = "Ordenes"
        const val COL_ID_ORDEN = "idOrden"
        const val COL_ID_CLIENTE_FK = "idCliente"
        const val COL_FECHA = "fecha"
        const val COL_TOTAL = "total"

        // Tabla DetalleOrden
        const val TABLE_DETALLE = "DetalleOrden"
        const val COL_ID_DETALLE = "idDetalle"
        const val COL_ID_ORDEN_FK = "idOrden"
        const val COL_ID_PRODUCTO_FK = "idProducto"
        const val COL_CANTIDAD = "cantidad"
        const val COL_SUBTOTAL = "subtotal"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE_CLIENTES (
                $COL_ID_CLIENTE INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NOMBRE TEXT NOT NULL,
                $COL_CORREO TEXT NOT NULL,
                $COL_TELEFONO TEXT NOT NULL
            )
        """)

        db.execSQL("""
            CREATE TABLE $TABLE_PRODUCTOS (
                $COL_ID_PRODUCTO INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NOMBRE_PRODUCTO TEXT NOT NULL,
                $COL_PRECIO REAL NOT NULL,
                $COL_DESCRIPCION TEXT
            )
        """)

        db.execSQL("""
            CREATE TABLE $TABLE_ORDENES (
                $COL_ID_ORDEN INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_ID_CLIENTE_FK INTEGER NOT NULL,
                $COL_FECHA TEXT NOT NULL,
                $COL_TOTAL REAL NOT NULL,
                FOREIGN KEY ($COL_ID_CLIENTE_FK) REFERENCES $TABLE_CLIENTES($COL_ID_CLIENTE)
            )
        """)

        db.execSQL("""
            CREATE TABLE $TABLE_DETALLE (
                $COL_ID_DETALLE INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_ID_ORDEN_FK INTEGER NOT NULL,
                $COL_ID_PRODUCTO_FK INTEGER NOT NULL,
                $COL_CANTIDAD INTEGER NOT NULL,
                $COL_SUBTOTAL REAL NOT NULL,
                FOREIGN KEY ($COL_ID_ORDEN_FK) REFERENCES $TABLE_ORDENES($COL_ID_ORDEN),
                FOREIGN KEY ($COL_ID_PRODUCTO_FK) REFERENCES $TABLE_PRODUCTOS($COL_ID_PRODUCTO)
            )
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DETALLE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ORDENES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CLIENTES")
        onCreate(db)
    }

    // ==================== crud cliente ====================

    fun insertarCliente(nombre: String, correo: String, telefono: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NOMBRE, nombre)
            put(COL_CORREO, correo)
            put(COL_TELEFONO, telefono)
        }
        return db.insert(TABLE_CLIENTES, null, values)
    }

    fun obtenerClientes(): List<Map<String, String>> {
        val lista = mutableListOf<Map<String, String>>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_CLIENTES", null)
        while (cursor.moveToNext()) {
            val map = mapOf(
                "idCliente" to cursor.getString(cursor.getColumnIndexOrThrow(COL_ID_CLIENTE)),
                "nombre" to cursor.getString(cursor.getColumnIndexOrThrow(COL_NOMBRE)),
                "correo" to cursor.getString(cursor.getColumnIndexOrThrow(COL_CORREO)),
                "telefono" to cursor.getString(cursor.getColumnIndexOrThrow(COL_TELEFONO))
            )
            lista.add(map)
        }
        cursor.close()
        return lista
    }

    fun actualizarCliente(id: Int, nombre: String, correo: String, telefono: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NOMBRE, nombre)
            put(COL_CORREO, correo)
            put(COL_TELEFONO, telefono)
        }
        return db.update(TABLE_CLIENTES, values, "$COL_ID_CLIENTE=?", arrayOf(id.toString()))
    }

    fun eliminarCliente(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_CLIENTES, "$COL_ID_CLIENTE=?", arrayOf(id.toString()))
    }

    // ==================== crud productos ====================

    fun insertarProducto(nombre: String, precio: Double, descripcion: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NOMBRE_PRODUCTO, nombre)
            put(COL_PRECIO, precio)
            put(COL_DESCRIPCION, descripcion)
        }
        return db.insert(TABLE_PRODUCTOS, null, values)
    }

    fun obtenerProductos(): List<Map<String, String>> {
        val lista = mutableListOf<Map<String, String>>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PRODUCTOS", null)
        while (cursor.moveToNext()) {
            val map = mapOf(
                "idProducto" to cursor.getString(cursor.getColumnIndexOrThrow(COL_ID_PRODUCTO)),
                "nombreProducto" to cursor.getString(cursor.getColumnIndexOrThrow(COL_NOMBRE_PRODUCTO)),
                "precio" to cursor.getString(cursor.getColumnIndexOrThrow(COL_PRECIO)),
                "descripcion" to cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPCION))
            )
            lista.add(map)
        }
        cursor.close()
        return lista
    }

    fun actualizarProducto(id: Int, nombre: String, precio: Double, descripcion: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NOMBRE_PRODUCTO, nombre)
            put(COL_PRECIO, precio)
            put(COL_DESCRIPCION, descripcion)
        }
        return db.update(TABLE_PRODUCTOS, values, "$COL_ID_PRODUCTO=?", arrayOf(id.toString()))
    }

    fun eliminarProducto(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_PRODUCTOS, "$COL_ID_PRODUCTO=?", arrayOf(id.toString()))
    }

    // ==================== CRUD ORDENES ====================

    fun insertarOrden(idCliente: Int, fecha: String, total: Double): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_ID_CLIENTE_FK, idCliente)
            put(COL_FECHA, fecha)
            put(COL_TOTAL, total)
        }
        return db.insert(TABLE_ORDENES, null, values)
    }

    fun obtenerOrdenes(): List<Map<String, String>> {
        val lista = mutableListOf<Map<String, String>>()
        val db = readableDatabase
        val cursor = db.rawQuery("""
            SELECT o.$COL_ID_ORDEN, c.$COL_NOMBRE, o.$COL_FECHA, o.$COL_TOTAL
            FROM $TABLE_ORDENES o
            INNER JOIN $TABLE_CLIENTES c ON o.$COL_ID_CLIENTE_FK = c.$COL_ID_CLIENTE
        """, null)
        while (cursor.moveToNext()) {
            val map = mapOf(
                "idOrden" to cursor.getString(0),
                "cliente" to cursor.getString(1),
                "fecha" to cursor.getString(2),
                "total" to cursor.getString(3)
            )
            lista.add(map)
        }
        cursor.close()
        return lista
    }

    // ==================== CRUD DETALLE ORDEN ====================

    fun insertarDetalle(idOrden: Long, idProducto: Int, cantidad: Int, subtotal: Double): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_ID_ORDEN_FK, idOrden)
            put(COL_ID_PRODUCTO_FK, idProducto)
            put(COL_CANTIDAD, cantidad)
            put(COL_SUBTOTAL, subtotal)
        }
        return db.insert(TABLE_DETALLE, null, values)
    }

    fun obtenerDetalleOrden(idOrden: Int): List<Map<String, String>> {
        val lista = mutableListOf<Map<String, String>>()
        val db = readableDatabase
        val cursor = db.rawQuery("""
            SELECT p.$COL_NOMBRE_PRODUCTO, d.$COL_CANTIDAD, p.$COL_PRECIO, d.$COL_SUBTOTAL
            FROM $TABLE_DETALLE d
            INNER JOIN $TABLE_PRODUCTOS p ON d.$COL_ID_PRODUCTO_FK = p.$COL_ID_PRODUCTO
            WHERE d.$COL_ID_ORDEN_FK = ?
        """, arrayOf(idOrden.toString()))
        while (cursor.moveToNext()) {
            val map = mapOf(
                "nombreProducto" to cursor.getString(0),
                "cantidad" to cursor.getString(1),
                "precio" to cursor.getString(2),
                "subtotal" to cursor.getString(3)
            )
            lista.add(map)
        }
        cursor.close()
        return lista
    }

    fun obtenerClientesParaSpinner(): List<Map<String, String>> {
        val lista = mutableListOf<Map<String, String>>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT $COL_ID_CLIENTE, $COL_NOMBRE FROM $TABLE_CLIENTES", null)
        while (cursor.moveToNext()) {
            val map = mapOf(
                "idCliente" to cursor.getString(0),
                "nombre" to cursor.getString(1)
            )
            lista.add(map)
        }
        cursor.close()
        return lista
    }

    fun obtenerProductosParaSpinner(): List<Map<String, String>> {
        val lista = mutableListOf<Map<String, String>>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT $COL_ID_PRODUCTO, $COL_NOMBRE_PRODUCTO, $COL_PRECIO FROM $TABLE_PRODUCTOS", null)
        while (cursor.moveToNext()) {
            val map = mapOf(
                "idProducto" to cursor.getString(0),
                "nombreProducto" to cursor.getString(1),
                "precio" to cursor.getString(2)
            )
            lista.add(map)
        }
        cursor.close()
        return lista
    }
}