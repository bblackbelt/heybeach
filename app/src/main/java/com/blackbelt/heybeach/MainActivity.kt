package com.blackbelt.heybeach

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.blackbelt.heybeach.net.RequestExecutor
import com.blackbelt.heybeach.net.TaskFactory
import com.blackbelt.heybeach.net.TaskListener

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val task = TaskFactory.createBeachTask(1, object : TaskListener<String> {
            override fun onTaskCompleted(result: String) {
                Log.e("TEST", "TEST $result")
            }

            override fun onTaskFailed(message: String?, throwable: Throwable?) {
                throwable?.printStackTrace()
            }
        })

        RequestExecutor.getInstance().executeTask(task)
    }
}
