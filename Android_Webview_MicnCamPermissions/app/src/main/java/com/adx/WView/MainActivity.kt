
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.net.http.SslError
import android.os.Bundle
import android.webkit.PermissionRequest
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.adx.bhurd.ui.theme.BHurdTheme


open class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if (!checkPermission()) {
                requestPermission();
            }
            BHurdTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        val myWebView = WebView(this)
        myWebView.overScrollMode = WebView.OVER_SCROLL_NEVER
        myWebView.settings.setJavaScriptEnabled(true)
        myWebView.settings.javaScriptEnabled = true
        myWebView.settings.mediaPlaybackRequiresUserGesture = false
        myWebView.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(
                view: WebView,
                handler: SslErrorHandler,
                error: SslError
            ) {
                handler.proceed() // Ignore SSL certificate errors
            }
        }
        myWebView.webChromeClient = object : WebChromeClient() {

            override fun onPermissionRequest(request: PermissionRequest?) {

                    request?.let { request ->
                    if (request.resources.contains(PermissionRequest.RESOURCE_VIDEO_CAPTURE)) {
                        request.grant(request.resources)
                        return
                    } else if (request.resources.contains(PermissionRequest.RESOURCE_AUDIO_CAPTURE)) {
                        request.grant(request.resources)
                        return
                    }
                }

                super.onPermissionRequest(request)
            }
            }
        }

        setContentView(myWebView)
        myWebView.loadUrl("https://example.com:443")
    }

    fun checkPermission(): Boolean {
        if ( (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            && (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) ) {
            // Permission is not granted
            return false
        } else {
            return true
        }
    }

    fun requestPermission() {
        var PERMISSION_REQUEST_CODE = 1
        ActivityCompat.requestPermissions(
            this, arrayOf<String>(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA),
            PERMISSION_REQUEST_CODE
        )
    }
