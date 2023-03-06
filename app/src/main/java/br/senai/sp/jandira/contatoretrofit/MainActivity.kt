package br.senai.sp.jandira.contatoretrofit

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.senai.sp.jandira.contatoretrofit.model.Contact
import br.senai.sp.jandira.contatoretrofit.server.ContactCall
import br.senai.sp.jandira.contatoretrofit.server.RetrofitAPI
import br.senai.sp.jandira.contatoretrofit.ui.theme.ContatoRetrofitTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      ContatoRetrofitTheme {
        // A surface container using the 'background' color from the theme
        Surface(
          modifier = Modifier
            .fillMaxSize(),
          color = MaterialTheme.colors.background
        ) {
          Global()
        }
      }
    }
  }
}

@Composable
fun Global() {
  val retrofit = RetrofitAPI.getRetrofitIntance()
  val contactsCall = retrofit.create(ContactCall::class.java)
  val getAllContactsCall = contactsCall.getAllContacts()

  var contacts by rememberSaveable {
    mutableStateOf(listOf<Contact>())
  }

  getAllContactsCall.enqueue(object : Callback<List<Contact>> {
    override fun onResponse(call: Call<List<Contact>>, response: Response<List<Contact>>) {
      contacts = response.body()!!
    }

    override fun onFailure(call: Call<List<Contact>>, t: Throwable) {
      Log.i("ERROR DS3M", t.message.toString())
    }
  })

  var nameState by rememberSaveable {
    mutableStateOf("")
  }

  var emailState by rememberSaveable {
    mutableStateOf("")
  }

  var phoneState by rememberSaveable {
    mutableStateOf("")
  }

  var activeState by rememberSaveable {
    mutableStateOf(false)
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colors.background)
      .padding(12.dp)
  ) {
    OutlinedTextField(
      value = nameState,
      onValueChange = { nameState = it },
      modifier = Modifier.fillMaxWidth(),
      label = { Text(text = "Name") }
    )
    OutlinedTextField(
      value = emailState,
      onValueChange = { emailState = it },
      modifier = Modifier.fillMaxWidth(),
      label = { Text(text = "E-mail") }
    )
    OutlinedTextField(
      value = phoneState,
      onValueChange = { phoneState = it },
      modifier = Modifier.fillMaxWidth(),
      label = { Text(text = "Phone Number") }
    )
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      Checkbox(checked = activeState, onCheckedChange = { activeState = it })
      Text(text = "Active")
    }
    OutlinedButton(
      onClick = {
        val contact = Contact(0, nameState, emailState, phoneState, activeState)
        val createContactCall = contactsCall.createContact(contact)

        createContactCall.enqueue(object : Callback<Contact> {
          override fun onResponse(call: Call<Contact>, response: Response<Contact>) {
            Log.i("DS3M", "USUÁRIO CRIADO COM SUCESSO, ID: ${response.body()!!.id}")
          }

          override fun onFailure(call: Call<Contact>, t: Throwable) {
            Log.i("DS3M", "ERRO")
          }
        })
      },
      modifier = Modifier.fillMaxWidth()
    ) {
      Text(text = "Create")
    }
    LazyColumn(
      modifier = Modifier.fillMaxWidth()
    ) {
      items(contacts) {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable {
              nameState = it.name
            },
          shape = RoundedCornerShape(4.dp),
          backgroundColor = MaterialTheme.colors.primary
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = it.name,
              color = MaterialTheme.colors.onPrimary
            )
            Text(
              text = it.email,
              color = MaterialTheme.colors.onPrimary
            )
            Text(
              text = it.phone,
              color = MaterialTheme.colors.onPrimary
            )
            Text(
              text = "${it.active}",
              color = MaterialTheme.colors.onPrimary
            )
            IconButton(
              onClick = {
                val deleteUserCall = contactsCall.deleteContact(it.id)
                deleteUserCall.enqueue(object: Callback<Boolean> {
                  override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    Log.i("DS3M", "ITEM DELETADO COM SUCESSO")
                  }

                  override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Log.i("DS3M", "FALHA, ${t.message.toString()}")
                  }
                })
              },
              modifier = Modifier.background(
                MaterialTheme.colors.error
              )
            ) {
              Icon(imageVector = Icons.Filled.Close, contentDescription = "")
            }
          }
        }
        Spacer(modifier = Modifier.height(12.dp))
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  ContatoRetrofitTheme {
    Global()
  }
}