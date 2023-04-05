package com.example.soapwebservice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.*
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE

class MainActivity : AppCompatActivity() {


    lateinit var numInput: EditText
    lateinit var resText: TextView

    private val SOAP_ACTION = "https://www.dataaccess.com/webservicesserver/";
    private val NAMESPACE = "http://www.dataaccess.com/webservicesserver/";
    private val METHOD_NAME = "NumberToWords";
    private val PROPERTY = "ubiNum"
    private val URL_WDSL = "https://www.dataaccess.com/webservicesserver/numberconversion.wso?wsdl";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numInput = findViewById(R.id.numInput)
        resText = findViewById(R.id.resText)


    }

    fun getResult(): String? {
        val num = numInput.text.toString()

        val req: SoapObject = SoapObject(NAMESPACE, METHOD_NAME)
        req.addProperty(PROPERTY, num)

        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
        envelope.setOutputSoapObject(req)
//        envelope.dotNet = true


        try {
            val httpTransport = HttpTransportSE(URL_WDSL)
            httpTransport.call(SOAP_ACTION, envelope)
            val res = envelope.bodyIn as SoapObject

            return res.getProperty(0).toString()

        } catch (
            e: java.lang.Exception
        ) {
            Log.e("TAG", "onCreate: ${e.message}")
            return ""
        }

    }

    fun onclick(view: View) {

        resText.visibility = View.VISIBLE
        resText.text = "Loading..."


        GlobalScope.launch(Dispatchers.IO) {
            val res = getResult()
            delay(1000L)
            withContext(Dispatchers.Main) {

                if (res != null) {
                    resText.text = res
                    numInput.text.clear()

                } else {
                    resText.text = "no response"
                }
            }
        }
    }
}