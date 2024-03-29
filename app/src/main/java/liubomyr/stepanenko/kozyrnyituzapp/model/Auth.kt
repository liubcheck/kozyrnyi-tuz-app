package liubomyr.stepanenko.kozyrnyituzapp.model

data class AuthResponse(
    val token: String
)

data class AuthRequest(
    val email: String,
    val password: String
) {
    companion object {
        fun from(email: String, password: String): AuthRequest {
            return AuthRequest(email, password)
        }
    }
}