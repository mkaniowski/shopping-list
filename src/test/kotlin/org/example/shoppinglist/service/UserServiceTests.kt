import org.example.shoppinglist.model.network.RegisterUserRequest
import org.example.shoppinglist.repository.UserRepository
import org.example.shoppinglist.service.UserService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.resource.RealmResource
import org.keycloak.admin.client.resource.UsersResource
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import org.keycloak.representations.idm.UserRepresentation
import java.util.*
import jakarta.ws.rs.core.Response
import org.example.shoppinglist.config.KeycloakAdminConfig

class UserServiceTest {

    private lateinit var userRepository: UserRepository
    private lateinit var keycloakAdmin: Keycloak
    private lateinit var userService: UserService
    private lateinit var realmResource: RealmResource
    private lateinit var usersResource: UsersResource

    @BeforeEach
    fun setUp() {
        userRepository = mock(UserRepository::class.java)
        keycloakAdmin = mock(Keycloak::class.java)
        realmResource = mock(RealmResource::class.java)
        usersResource = mock(UsersResource::class.java)
        userService = UserService(userRepository, keycloakAdmin)

        `when`(keycloakAdmin.realm(anyString())).thenReturn(realmResource)
        `when`(realmResource.users()).thenReturn(usersResource)
    }

    @Test
    fun createUserSuccessfully() {
        val registerUserRequest = RegisterUserRequest("username", "email@example.com", "password", "firstName", "lastName")
        val userRepresentation = UserRepresentation().apply {
            id = UUID.randomUUID().toString()
        }

        `when`(usersResource.create(any(UserRepresentation::class.java))).thenReturn(Response.status(201).build())
        `when`(usersResource.search(anyString())).thenReturn(listOf(userRepresentation))

        val response = userService.createUser(registerUserRequest)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.data)
    }

    @Test
    fun createUserConflict() {
        val registerUserRequest = RegisterUserRequest("username", "email@example.com", "password", "firstName", "lastName")

        `when`(usersResource.create(any(UserRepresentation::class.java))).thenReturn(Response.status(409).build())

        val response = userService.createUser(registerUserRequest)

        assertEquals(HttpStatus.CONFLICT, response.status)
        assertNull(response.data)
    }

    @Test
    fun createUserInternalServerError() {
        val registerUserRequest = RegisterUserRequest("username", "email@example.com", "password", "firstName", "lastName")

        `when`(usersResource.create(any(UserRepresentation::class.java))).thenReturn(Response.status(500).build())

        val response = userService.createUser(registerUserRequest)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.status)
        assertNull(response.data)
    }

    @Test
    fun createUserKeycloakUserIdNotFound() {
        val registerUserRequest = RegisterUserRequest("username", "email@example.com", "password", "firstName", "lastName")

        `when`(usersResource.create(any(UserRepresentation::class.java))).thenReturn(Response.status(201).build())
        `when`(usersResource.search(anyString())).thenReturn(emptyList())

        val response = userService.createUser(registerUserRequest)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.status)
        assertNull(response.data)
    }
}