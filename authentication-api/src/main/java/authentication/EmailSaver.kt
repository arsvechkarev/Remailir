package authentication

interface EmailSaver {
  
  fun getEmail(): String?
  
  fun saveEmail(email: String)
  
  fun deleteEmailSynchronously()
}