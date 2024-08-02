package net.azeti.challenge.recipe.controller


import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import net.azeti.challenge.recipe.config.TestConfig
import net.azeti.challenge.recipe.recipe.RecipeManagement
import net.azeti.challenge.recipe.recipe.model.Ingredient
import net.azeti.challenge.recipe.recipe.model.Recipe
import net.azeti.challenge.recipe.recipe.model.RecipeDto
import net.azeti.challenge.recipe.recipe.model.RecipeModelMapper
import net.azeti.challenge.recipe.recipe.model.Unit
import net.azeti.challenge.recipe.user.service.UserServiceImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.Optional

@ExtendWith(SpringExtension::class)
@WebMvcTest(RecipeManagementController::class)
@Import(TestConfig::class)
class RecipeManagementControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var recipeManagement: RecipeManagement

    @MockBean
    private lateinit var userService: UserServiceImpl

    private val mapper = RecipeModelMapper()
    private val objectMapper = jacksonObjectMapper()

    @BeforeEach
    fun setUp() {
        val userDetails = org.springframework.security.core.userdetails.User(
            "testUser",
            "password",
            mutableListOf()
        )
        `when`(userService.loadUserByUsername("testUser")).thenReturn(userDetails)
    }

    @Test
    @WithMockUser(username = "testUser")
    fun `should return recipe by id`() {
        val recipe = Recipe(
            id = 1L,
            title = "Test Recipe",
            description = "Test Description",
            ingredients = mutableListOf(),
            instructions = "Test Instructions",
            serving = 4
        )
        `when`(recipeManagement.getById(1L)).thenReturn(Optional.of(recipe))

        mockMvc.perform(
            get("/api/recipe/1")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.title").value("Test Recipe"))

        verify(recipeManagement).getById(1L)
    }

    @Test
    @WithMockUser(username = "testUser")
    fun `should return user recipes`() {
        val recipes = listOf(
            Recipe(
                id = 1L,
                title = "Recipe 1",
                description = "Description 1",
                ingredients = mutableListOf(),
                instructions = "Instructions 1",
                serving = 4
            ),
            Recipe(
                id = 2L,
                title = "Recipe 2",
                description = "Description 2",
                ingredients = mutableListOf(),
                instructions = "Instructions 2",
                serving = 2
            )
        )
        `when`(recipeManagement.getByUser("testUser")).thenReturn(recipes)

        mockMvc.perform(
            get("/api/recipe/byUserName")
                .param("username", "testUser")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[1].id").value(2L))

        verify(recipeManagement).getByUser("testUser")
    }

    @Test
    @WithMockUser(username = "testUser")
    fun `should create recipe`() {
        val recipeDto = RecipeDto(
            title = "New Recipe",
            description = "New Description",
            ingredients = "100 g Ingredient1",
            instructions = "New Instructions",
            serving = 3
        )
        val recipe = mapper.dtoToEntity(recipeDto).copy(id = 1L)
        `when`(recipeManagement.create(any())).thenReturn(recipe)

        mockMvc.perform(
            post("/api/recipe/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipeDto))
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.title").value("New Recipe"))

        verify(recipeManagement).create(any())
    }


    @Test
    @WithMockUser(username = "testUser")
    fun `should update recipe`() {
        val recipe = Recipe(
            id = 1L,
            title = "Test Recipe",
            description = "Test Description",
            ingredients = mutableListOf(Ingredient(unit = Unit.MILLILITER, type = "water")),
            instructions = "Test Instructions",
            serving = 4
        )
        `when`(recipeManagement.update(anyLong(), any())).thenReturn(recipe)

        mockMvc.perform(
            put("/api/recipe/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mapper.entityToDto(recipe)))
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.title").value("Test Recipe"))

        verify(recipeManagement).update(anyLong(), any())
    }

    @Test
    @WithMockUser(username = "testUser")
    fun `should delete recipe`() {
        val recipe = Recipe(
            id = 1L,
            title = "Test Recipe",
            description = "Test Description",
            ingredients = mutableListOf(),
            instructions = "Test Instructions",
            serving = 4
        )
        `when`(recipeManagement.delete(1L)).thenReturn(recipe)

        mockMvc.perform(
            delete("/api/recipe/1")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.title").value("Test Recipe"))

        verify(recipeManagement).delete(1L)
    }

    @Test
    @WithMockUser(username = "testUser")
    fun `should return not found for non-existing recipe`() {
        `when`(recipeManagement.getById(999L)).thenReturn(Optional.empty())

        mockMvc.perform(
            get("/api/recipe/999")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)

        verify(recipeManagement).getById(999L)
    }
}