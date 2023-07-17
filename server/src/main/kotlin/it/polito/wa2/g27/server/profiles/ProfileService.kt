package it.polito.wa2.g27.server.profiles


interface ProfileService {
    fun getByEmail(email: String): ProfileDTO
    fun getById(id: Int): ProfileDTO
    fun getAllByRole(role: String): List<ProfileDTO>

}