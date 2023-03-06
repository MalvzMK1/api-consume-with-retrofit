package br.senai.sp.jandira.contatoretrofit.model

data class Contact(
  val id: Int = 0,
  val name: String,
  val email: String,
  val phone: String,
  val active: Boolean,
) {
  override fun toString(): String {
    return "Contact(int=$id, name='$name', email='$email', phone='$phone', active=$active)"
  }
}
