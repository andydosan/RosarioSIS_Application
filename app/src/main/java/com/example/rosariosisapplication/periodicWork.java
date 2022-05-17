package com.example.rosariosisapplication;

import static com.example.rosariosisapplication.firstFragment.classGrades;
import static com.example.rosariosisapplication.firstFragment.counter;
import static com.example.rosariosisapplication.firstFragment.jsoupScraper;
import static com.example.rosariosisapplication.firstFragment.savedGrades;
import static com.example.rosariosisapplication.firstFragment.savedToFile;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.WorkInfo;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


public class periodicWork extends Worker {

    private static final String TAG = "yoonThePeriodicWork";
    private String CHANNEL_ID = "873";
    private static int notificationNum = 0;
    private static int notificationNumTest = 0;

    public periodicWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    @NonNull
    @Override


    public Result doWork() {
        createNotificationChannel();
        if (counter >=0 || !savedToFile) {
            jsoupScraper();
            if (savedGrades.equals(classGrades.toString())) {
                Log.d("yoon", "equal");
                //notification below is for testing reasons should be moved down underneath the else
                NotificationCompat.Builder builderTest = new NotificationCompat.Builder(this.getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.rosariosisthing)
                        .setContentTitle("Rosarosis")
                        .setContentText("Your grades have NOT been updated!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                NotificationManagerCompat notificationManagerTest = NotificationManagerCompat.from(getApplicationContext());
                notificationManagerTest.notify(notificationNumTest, builderTest.build());
                notificationNumTest++;
            } else {
                SharedPreferences settings = getApplicationContext().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("toString classGrades", classGrades.toString());
                editor.commit();
                Log.d("yoon", "it's been saved hopefully");

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.rosariosisthing)
                        .setContentTitle("Rosarosis")
                        .setContentText("Your grades have been updated!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                notificationManager.notify(notificationNum, builder.build());
                notificationNum++;
            }
            Log.d("yoon", classGrades.toString());
            Log.e(TAG, "doWork: Work is done.");


        }
        return Result.success();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "grade updated channel name";
            String description = "grade updated channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = this.getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
