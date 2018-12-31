package com.enpassio.apis.googlespreadsheet

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.enpassio.apis.BuildConfig
import com.enpassio.apis.R
import com.enpassio.apis.ServiceGenerator
import com.enpassio.apis.googlespreadsheet.model.ListSpreadsheet
import com.enpassio.apis.googlespreadsheet.model.ValueRange
import java.io.*
import java.util.*

//Java Excel ibrary reference for spreadsheet: https://www.studytutorial.in/android-export-sqlite-data-into-excel-sheet-xls-using-java-excel-library
class GoogleSpreadsheetActivity : AppCompatActivity() {

    private val DRIVE_SCOPE = "https://www.googleapis.com/auth/drive.readonly   https://www.googleapis.com/auth/spreadsheets.readonly"
    var myExternalFile: File? = null
    var myData = ""
    var file: File? = null
    private val MY_PERMISSIONS_REQUEST_READ_WRITE_STORAGE = 101
    private var isPermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_spreadsheet)
        val settings = getSharedPreferences("token", Context.MODE_PRIVATE)
        val token = settings.getString("token", "")!!
        //getListOfSpreadsheets(token)
        val writeData = findViewById<Button>(R.id.write_data)
        val readData: Button = findViewById(R.id.read_data)
        val writeToExternalStorageButton: Button = findViewById(R.id.write_to_external_storage_button)
        writeToExternalStorageButton.setOnClickListener { startActivityWriteToExternalActivity() }

        val createAndReadSpreadsheetUsingApachePOI: Button = findViewById(R.id.apache_poi_button)
        createAndReadSpreadsheetUsingApachePOI.setOnClickListener { startActivityCreateAndReadSpreadsheetUsingApachePOI() }

        writeData.setOnClickListener {
            val handler = Handler(Looper.getMainLooper())

            handler.postDelayed({
                // Run your task here
                checkReadWritePermission()
                createAndHandleExcelSpreadsheetFileJavaExcelLibrary()
            }, 1000)
        }
        readData.setOnClickListener {

            val handler = Handler(Looper.getMainLooper())

            handler.postDelayed({
                // Run your task here
                if (checkReadWritePermission()) {
                    readDataFromSpreadsheet()
                }
            }, 1000)
        }
    }

    private fun startActivityCreateAndReadSpreadsheetUsingApachePOI() {
        startActivity(Intent(this@GoogleSpreadsheetActivity, CreateAndReadSpreadsheetUsingApachePOI::class.java))
    }

    fun checkReadWritePermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, so ask for permission
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_READ_WRITE_STORAGE)

                //MY_PERMISSIONS_REQUEST_READ_WRITE_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        } else {
            //permission is already granted
            isPermissionGranted = true
        }
        return isPermissionGranted
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_WRITE_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    isPermissionGranted = true
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    isPermissionGranted = false
                }
            }
        }
    }

    private fun startActivityWriteToExternalActivity() {
        startActivity(Intent(this@GoogleSpreadsheetActivity, WriteToExternalStorage::class.java))
    }

    private fun readDataFromSpreadsheet() {
        try {
            val fis = FileInputStream(file)
            val `in` = DataInputStream(fis)
            val br = BufferedReader(InputStreamReader(`in`))
            var strLine = ""
            while ((strLine.equals(br.readLine())) != null) {
                myData = myData + strLine
            }
            `in`.close()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        Log.d("my_tag", "data fetched from file is: " + myData)
    }

    private fun createAndHandleExcelSpreadsheetFileJavaExcelLibrary() {
        val sd = Environment.getExternalStorageDirectory();
        val csvFile = "myData.xls";

        val directory = File(sd.getAbsolutePath());
        //create directory if not exist
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }
        //define workbook settings

        try {
            //file path
            file = File(directory, csvFile)
            val wbSettings = WorkbookSettings()
            wbSettings.locale = Locale("en", "EN")
            val workbook: WritableWorkbook
            workbook = Workbook.createWorkbook(file, wbSettings)

            //Excel sheet name. 0 represents first sheet
            val sheet = workbook.createSheet("propel response", 0)
            // column and row
            sheet.addCell(Label(0, 0, "UserName"))
            sheet.addCell(Label(1, 0, "PhoneNumber"))
            sheet.addCell(Label(0, 1, "Abhishek Raj"))
            sheet.addCell(Label(1, 1, "8870048900"))
            workbook.write();
            workbook.close();
            Toast.makeText(getApplication(),
                    "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("my_tag", "error in workbook setup: " + e.message)
        }
    }

    private fun isExternalStorageReadOnly(): Boolean {
        val extStorageState = Environment.getExternalStorageState()
        return if (Environment.MEDIA_MOUNTED_READ_ONLY == extStorageState) {
            true
        } else false
    }

    private fun isExternalStorageAvailable(): Boolean {
        val extStorageState = Environment.getExternalStorageState()
        return if (Environment.MEDIA_MOUNTED == extStorageState) {
            true
        } else false
    }

    private fun getListOfSpreadsheets(token: String) {
        val tokenForUser = token
        val listSpreadsheetService = ServiceGenerator.createService(ListSpreadsheetService::class.java, tokenForUser)
        val listSpreadsheetCall = listSpreadsheetService.getSpreadsheetList("application/vnd.google-apps.spreadsheet", DRIVE_SCOPE,
                BuildConfig.GOOGLE_API_CLIENT_ID)
        listSpreadsheetCall.enqueue(object : Callback<ListSpreadsheet> {
            override fun onResponse(call: Call<ListSpreadsheet>, response: Response<ListSpreadsheet>) {
                val listOfSpreadsheet = response.body()

                Log.v("my_taggggg", "token inside spreadsheet data received is: " + tokenForUser)
                Log.v("my_taggggg", "listOfSpreadsheet data received is: " + listOfSpreadsheet)
                Log.v("my_taggggg", "response.errorBody() is: " + listOfSpreadsheet)
                Log.v("my_taggggg", "response.message() is: " + response.message())
                Log.v("my_taggggg", "response.code() is: " + response.code())
                Log.v("my_taggggg", "response.headers() is: " + response.headers())
                Log.v("my_taggggg", "response.raw() is: " + response.raw())
                for (file in listOfSpreadsheet?.files!!) {
                    Log.v("my_taggggg", "spreadsheet id is: " + file.id)
//                    callForSingleSpreadsheetData(file.id, token)
                }
                callForSingleSpreadsheetData(listOfSpreadsheet.files!!.get(0).id, token)
            }

            override fun onFailure(call: Call<ListSpreadsheet>, t: Throwable) {
                Log.e("my_taggggg", "error is: " + t.message)
            }
        })
    }

    private fun callForSingleSpreadsheetData(id: String?, token: String) {
        val spreadsheetService = SpreadsheetServiceGenerator.createService(SpreadsheetService::class.java, token)
        val spreadsheetCall = spreadsheetService.getSpreadsheetData("10uRokjEVBWRdE7wHas_An5nXFU01tO_kuZzWjQbIJBI", "A1:Z")
        spreadsheetCall.enqueue(object : Callback<ValueRange> {
            override fun onFailure(call: Call<ValueRange>, t: Throwable) {
                Log.e("my_taggsss", "single error is: " + t.message)
            }

            override fun onResponse(call: Call<ValueRange>, response: Response<ValueRange>) {
                val valueRange = response.body()
                if (valueRange?.values != null) {
                    for (singleValue in valueRange.values!!) {
                        if (singleValue.size > 0) {
                            Log.v("my_taggsss", "Spreadsheet data value is: " + singleValue)

                        }
                    }
                }
            }
        })
    }

    private inner class LongOperation : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String): String {
            myData = ""
            if (params.get(0).equals("write")) {
                createAndHandleExcelSpreadsheetFileJavaExcelLibrary()
            } else {
                readDataFromSpreadsheet()
            }
            return myData
        }

        override fun onPostExecute(result: String) {
            Log.d("my_tag", "data fetched from file is: " + result)
        }

        override fun onPreExecute() {}

        override fun onProgressUpdate(vararg values: Void) {}
    }
}
