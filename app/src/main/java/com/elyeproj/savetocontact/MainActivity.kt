package com.elyeproj.savetocontact

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds
import android.view.View
import android.view.View.MeasureSpec
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contact_save_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI).apply {
                type = ContactsContract.RawContacts.CONTENT_TYPE
                putExtra(ContactsContract.Intents.Insert.NAME, contact_name.text)
                putExtra(ContactsContract.Intents.Insert.EMAIL, contact_email.text)
                putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, CommonDataKinds.Email.TYPE_WORK)
                putExtra(ContactsContract.Intents.Insert.PHONE, contact_phone.text)
                putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, CommonDataKinds.Phone.TYPE_WORK)

                val imageBitmap = getScreenViewBitmap(contact_image)
                val row = ContentValues().apply {
                    put(CommonDataKinds.Photo.PHOTO, bitmapToByteArray(imageBitmap))
                    put(ContactsContract.Data.MIMETYPE, CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                }
                val data = arrayListOf(row)

                putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, data)
            }

            startActivity(intent)
        }

    }

    private fun getScreenViewBitmap(view: View): Bitmap {
        val specSize = MeasureSpec.makeMeasureSpec(0 /* any */, MeasureSpec.UNSPECIFIED)
        view.measure(specSize, specSize)
        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.layout(view.left, view.top, view.right, view.bottom)
        view.draw(canvas)
        return bitmap
    }



    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        return stream.toByteArray()
    }
}
