package com.example.videoplayer

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.videoplayer.databinding.ActivityMainBinding
import java.io.File
import java.lang.Exception
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle:ActionBarDrawerToggle

    companion object{
        lateinit var videoList: ArrayList<Video>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setTheme(R.style.coolBlueNav)

        setContentView(binding.root)



        //for Nav Drawer
        toggle = ActionBarDrawerToggle(this, binding.root, R.string.open, R.string.close)
        binding.root.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        videoList = getAllVideos()

        //the default fragment
        setFragment(VideosFragment())


//        if(requestRuntimePermission()){
//            videoList = getAllVideos()
//
//            //the default fragment
//            setFragment(VideosFragment())
//        }



        //listener that that inform us when any item is clicked
        binding.bottomNav.setOnItemSelectedListener {

            when(it.itemId){
                //when videoView nav button is clicked then set the video Fragment, and the same for the foldersView nav button
                R.id.videoView -> setFragment(VideosFragment())
                R.id.foldersView -> setFragment((FoldersFragment()))
            }

             return@setOnItemSelectedListener true
        }

        //set listener on navigation items in the top Nav View
        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){

                R.id.feedbackNav -> Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show()
                R.id.themesNav -> Toast.makeText(this, "Themes", Toast.LENGTH_SHORT).show()
                R.id.sortOrderNav -> Toast.makeText(this, "Sort Order", Toast.LENGTH_SHORT).show()
                R.id.aboutNav -> Toast.makeText(this, "App Developed by Bassem Abou Daher", Toast.LENGTH_SHORT).show()
                R.id.exitNav -> exitProcess(1)
            }

            return@setNavigationItemSelectedListener true
        }
    }

    //function that allows to set a specified Fragment
    private fun setFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()

        //change the content of the frameLayout "fragmentFL" and replace it with the inputted fragment
        transaction.replace(R.id.fragmentFL, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }

    //function to request permission
    private fun requestRuntimePermission():Boolean {

        //first check if the WRITE_EXTERNAL_STORAGE is not given, them request it
        if (ActivityCompat.checkSelfPermission(
                this,
                WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE), 5)
            return false
        }
        return true
    }

    //override the permissions result to manage the case when a permission result with request code 5 comes (as above)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 5){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else{
                ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE), 5)
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("Range")
    private fun getAllVideos(): ArrayList<Video>{
        val tempList = ArrayList<Video>()
        val projection = arrayOf(MediaStore.Video.Media.TITLE, MediaStore.Video.Media.SIZE, MediaStore.Video.Media._ID,
                                MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATA,
                                MediaStore.Video.Media.DATE_ADDED, MediaStore.Video.Media.DURATION)
        val cursor = this.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null,
        MediaStore.Video.Media.DATE_ADDED+" DESC")
        if(cursor != null){
            if(cursor.moveToNext()){
                do {
                    val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))
                    val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID))
                    val folderC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                    val sizeC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))
                    val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
                    val durationC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION)).toLong()

                    try {
                        val file = File(pathC)
                        val artUriC = Uri.fromFile(file)
                        val video = Video(title = titleC, id = idC, folderName = folderC, duration = durationC, size = sizeC, path = pathC, artUri = artUriC)
                        if(file.exists()){
                            tempList.add(video)
                        }

                    }catch (e: Exception){}

                }while (cursor.moveToNext())
                cursor?.close()
            }
        }
        return tempList
    }

}