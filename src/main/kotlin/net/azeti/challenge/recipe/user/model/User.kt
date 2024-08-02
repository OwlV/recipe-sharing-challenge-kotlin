package net.azeti.challenge.recipe.user

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@Entity(name = "users")
@Table(
    name = "users",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["username"]),
        UniqueConstraint(columnNames = ["email"])
    ]
)
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    @NotBlank @Size(max = 20) private val username: String? = null,
    @NotBlank @Size(max = 50) @Email val email: String? = null,
    @NotBlank @Size(max = 120) private val password: String? = null
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = Collections.emptySet()
    override fun getPassword(): String? = password
    override fun getUsername(): String? = username
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true

}
