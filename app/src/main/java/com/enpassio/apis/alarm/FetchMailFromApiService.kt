package com.enpassio.apis.alarm

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.enpassio.apis.*


class FetchMailFromApiService : Service {

    companion object {
        val TAG: String = FetchMailFromApiService::class.java.simpleName
        // Scope for reading user's contacts
        private val CONTACTS_SCOPE = "https://mail.google.com/+https://www.googleapis.com/auth/userinfo.email"
        private val redirectUri = BuildConfig.REDIRECT_URI
    }

    constructor() {}

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.v("my_tag", "FetchMailFromApiService onStartCommand called")
        fetchListOfIDsOfMail()

        return START_STICKY
    }

    fun fetchListOfIDsOfMail() {
        checkIfTokenIsAvailable()
    }

    //check if token exist already or not and act accordingly
    private fun checkIfTokenIsAvailable() {

        val settings = getSharedPreferences("token", Context.MODE_PRIVATE)
        val token = settings.getString("token", "")!!

        getAccessTokenFromRefreshToken(token)
        if (!token.isEmpty()) {
            getListOfMail(token)
        }
        Log.v("my_tag", "token inside FetchMailFromApiService is: " + token)
    }

    private fun getListOfMail(response: String) {
        val tokenForUser = response
        val mailListService = ServiceGenerator.createService(GmailService::class.java, tokenForUser)
        val mailCall = mailListService.getListOfEmails("me", CONTACTS_SCOPE,
                BuildConfig.GOOGLE_API_CLIENT_ID,
                redirectUri)
        mailCall.enqueue(object : Callback<ListOfMailIds> {
            override fun onResponse(call: Call<ListOfMailIds>, response: Response<ListOfMailIds>) {
                val listOfIdsOfMails = response.body()
                /*
                Log.v("my_tag", "mail data received is: " + listOfIdsOfMails)

                Log.v("my_tag", "mail id is: " + listOfIdsOfMails?.messages?.get(0)?.id)
                */
                var count = 0
                for (singleMessage in listOfIdsOfMails?.messages!!) {
                    count = count + 1
                    getMessageFromMessageList("me", singleMessage.id,
                            CONTACTS_SCOPE,
                            BuildConfig.GOOGLE_API_CLIENT_ID,
                            redirectUri,
                            tokenForUser)
                    if (count == 5)
                        break
                }

                /*


                Log.v("my_tag", "response.errorBody() is: " + accessToken)
                Log.v("my_tag", "response.message() is: " + response.message())
                Log.v("my_tag", "response.code() is: " + response.code())
                Log.v("my_tag", "response.headers() is: " + response.headers())
                Log.v("my_tag", "response.raw() is: " + response.raw())
                */

            }

            override fun onFailure(call: Call<ListOfMailIds>, t: Throwable) {
                Log.e("my_tag", "error is: " + t.message)
            }
        })
    }

    private fun getAccessTokenFromRefreshToken(refreshToken: String) {
        val refreshTokenService = APIClient.client.create(RefreshTokenService::class.java)

        val call =
                refreshTokenService.getAccessTokenFromRfreshToken(
                        refreshToken,
                        BuildConfig.GOOGLE_API_CLIENT_ID,
                        "refresh_token")
        call.enqueue(object : Callback<AccessToken> {
            override fun onFailure(call: Call<AccessToken>, t: Throwable) {

            }

            override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                Log.v("my_tag", "token from refresh token is :" + response.body()?.accessToken)
            }
        })
    }

    private fun getMessageFromMessageList(userId: String, messageId: String?, scope: String, client_ID: String, redirectUri: String, tokenForUser: String?) {
        val singleMessageService = ServiceGenerator.createService(ParticularItemService::class.java, tokenForUser)
        val singleMessageCall = singleMessageService.getParticularEmail("me", messageId!!, scope,
                client_ID,
                redirectUri)
        singleMessageCall.enqueue(object : Callback<Message> {
            override fun onFailure(call: Call<Message>, t: Throwable) {

                Log.v("my_tag", "fail message is: " + t.message.toString())
            }

            override fun onResponse(call: Call<Message>, response: Response<Message>) {

                /*
                Log.v("my_tag", "response.errorBody() is: " + response.errorBody())
                Log.v("my_tag", "response.message() is: " + response.message())
                Log.v("my_tag", "response.code() is: " + response.code())
                Log.v("my_tag", "response.headers() is: " + response.headers())
                Log.v("my_tag", "response.raw() is: " + response.raw())

                Log.v("my_tag", "internalDate received: " + response.body()?.internalDate)
                */
                val date = response.body()?.internalDate
                var from = ""
                var subject = ""
                var encodedData = ""


                val payload: PayloadInMessage = response.body()?.payload!!
                val headersInsidePayload = payload.headers

                for (item in headersInsidePayload!!) {

                    val name = item.name
                    if (name.equals("From")) {
                        from = item.value!!
                    } else if (name.equals("Subject")) {
                        subject = item.value!!
                    }
                }

                /*
                Log.v("my_tag", "from : " + from)
                Log.v("my_tag", "decoded from : " + java.net.URLDecoder.decode(from, "UTF-8"))
                Log.v("my_tag", "subject: " + subject)
                Log.v("my_tag", "messageCreationTime in epoch is: " + date)

                */
                val time = DateTime(date?.toLong()!!)
                //Log.v("my_tag", "Time instance in local time-zone is :" + time)

                val partsInPayload = payload.parts
                //Log.v("my_tag", "parts is: " + partsInPayload)


                if (partsInPayload != null) {
                    //    Log.v("my_tag", "encoded size : " + partsInPayload.get(1).body?.size)
                    for (body in partsInPayload) {
                        if (body.mimeType.equals("text/plain")) {
                            encodedData = body.body?.data!!
                            Log.v("my_tag", "encoded data: " + encodedData)

                        }
                    }
                }
                /*
                val mailBody = Base64.decode(encodedData.trim(), Base64.DEFAULT)
                Log.v("my_tag", "actual message is: " + String(mailBody, Charsets.UTF_8))

                val urlParser = UrlDetector(String(mailBody, Charsets.UTF_8), UrlDetectorOptions.HTML);
                val detectedUrl = urlParser.detect().get(0).toString()

                val emailParser = UrlDetector(from, UrlDetectorOptions.HTML);
                val detectedMailFrom = emailParser.detect().get(0).toString()

                */

                val message_json = JSON.parse(message_data.to_json())

                mime_data = Base64.urlsafe_decode64(message_json['raw'])

                Log.v("my_tag", "_________________________________________")


            }
        })

    }

    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
    }

    interface GetMessageList {
        fun getMessageList(listOfMailIds: List<MessageWithIdAndThreadId>)
    }
}