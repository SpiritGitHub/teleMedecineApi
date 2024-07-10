//package com.sp.telemedecine.config;
//
//
//
//public class MyFirebaseMessagingService extends FirebaseMessagingService {
//
//    private static final String TAG = "MyFirebaseMsgService";
//
//    @Override
//    public void onNewToken(String token) {
//        super.onNewToken(token);
//        Log.d(TAG, "Refreshed token: " + token);
//        // Envoyer le token au serveur
//        sendRegistrationToServer(token);
//    }
//
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//        // Traiter le message reçu
//    }
//
//    private void sendRegistrationToServer(String token) {
//        // Exemple de code pour envoyer le token au serveur
//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = new FormBody.Builder()
//                .add("token", token)
//                .add("userId", "userId") // Remplacez par l'ID utilisateur réel
//                .build();
//        Request request = new Request.Builder()
//                .url("https://your-server-url.com/api/register-token")
//                .post(body)
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//                Log.d(TAG, "Token enregistré sur le serveur");
//            }
//        });
//    }
//}
//
