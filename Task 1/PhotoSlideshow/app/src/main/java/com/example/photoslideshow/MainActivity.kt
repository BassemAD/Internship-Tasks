package com.example.photoslideshow

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.example.photoslideshow.ui.theme.PhotoSlideshowTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhotoSlideshowTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Greeting("Android")
//                }

                //state for picking a single image
                  var selectedImageUri by remember {
                    mutableStateOf<Uri?>(null)
                  }

                //state for picking multiple images
                var selectedImageUris by remember {
                    mutableStateOf<List<Uri>>(emptyList())
                }

                //launch the single photo picker
                val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.PickVisualMedia(),
                    onResult = {uri -> selectedImageUri = uri} //updates the selectedImageUri variable to the result of the picked photo
                )

                //launch the multiple photo picker
                val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.PickMultipleVisualMedia(),
                    onResult = {
                            uris -> selectedImageUris = uris
                            //DisplayPager(selectedImageUris)
                    } //updates the selectedImageUris list to the result of the picked photos
                )


                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ){
                    item{
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Button(onClick = {
                                singlePhotoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly) //launch the picker and request only the photos
                                )
                             }) {
                                Text(text = "Pick one photo")
                            }

                            Button(onClick = {
                                multiplePhotoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly) //launch the picker and request only the photos
                                )
                                 }) {
                                Text(text = "Pick multiple photos")
                            }

                        }
                    }

                    item{


                            Box(
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                if (selectedImageUris.isNotEmpty()) {
                                    val pagerState =
                                        rememberPagerState(pageCount = selectedImageUris.size)
                                    HorizontalPager(
                                        state = pagerState
                                    ) { index ->
                                        val uri = selectedImageUris[index]

                                        AsyncImage(
                                            model = uri,
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxWidth(),
                                            contentScale = ContentScale.Crop
                                        )

                                    }
                                }
                            }

                    }



                    item{
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                    }
//
//                    items(selectedImageUris){uri ->
//                        AsyncImage(
//                            model = uri,
//                            contentDescription = null,
//                            modifier = Modifier.fillMaxWidth(),
//                            contentScale = ContentScale.Crop
//                        )
//
//                    }

                }
            }
        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PhotoSlideshowTheme {
        Greeting("Android")
    }
}
