package com.example.projectofinalitv.repositories.propietario

import com.example.projectofinalitv.config.ConfigApp
import com.example.projectofinalitv.models.Propietario
import com.example.projectofinalitv.services.database.DatabaseManager
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class PropietarioRepositoryImplTest {
    private val configApp = ConfigApp().apply {
        APP_URL = "jdbc:mariadb://localhost:3306/empresaitvparatest?serverTimezone=UTC"
    }
    private val repositoryPropietario = PropietarioRepositoryImpl(DatabaseManager(configApp))

    private val propietarios = listOf(
        Propietario(
            dni = "12345678A",
            nombre = "Iván",
            apellidos = "Ronco Cebadera",
            telefono = "758374829",
            email = "ivan@gmail.com"
        ),
        Propietario(
            dni = "98765432Z",
            nombre = "Jia",
            apellidos = "Zheng",
            telefono = "893759402",
            email = "jia@gmail.com"
        )
    )

    private val propietario3 = Propietario(
        dni = "12345678B",
        nombre = "Kevin",
        apellidos = "David Matute",
        telefono = "758374829",
        email = "ivan@gmail.com"
    )



    @BeforeEach
    fun setUpBeforeEach() {
        propietarios.forEach {
            repositoryPropietario.save(it)
        }
    }

    @AfterAll
    fun tearDown(){
        repositoryPropietario.deleteAll()
    }
    @AfterEach
    fun tearDownAfterEach(){
        repositoryPropietario.deleteAll()
    }


    @Test
    fun getAll() {
        val res = repositoryPropietario.getAll()
        assertEquals(propietarios, res)
        assertTrue(res.size == 2)
        assertEquals(propietarios[0], res[0])
        assertEquals(propietarios[1], res[1])
    }

    @Test
    fun getById() {
        val res = repositoryPropietario.getById("12345678A")
        val res2 = repositoryPropietario.getById("12345678C")
        assertEquals(propietarios[0], res)
        assertEquals(null, res2)
    }

    @Test
    fun save() {
        val res = repositoryPropietario.save(propietario3)
        val savedPropietarios = repositoryPropietario.getAll()
        assertEquals(propietario3, res)
        assertTrue(savedPropietarios.size == 3)
    }

    @Test
    fun deleteById() {
        val res = repositoryPropietario.deleteById("12345678A")
        val res2 =  repositoryPropietario.deleteById("12345678C")
        val savedPropietarios = repositoryPropietario.getAll()
        assertEquals(true, res)
        assertFalse(res2)
        assertTrue(savedPropietarios.size == 1)
    }

    @Test
    fun deleteAll() {
        val res = repositoryPropietario.deleteAll()
        val savedPropietarios = repositoryPropietario.getAll()
        assertEquals(true, res)
        assertTrue(savedPropietarios.isEmpty())
    }
}