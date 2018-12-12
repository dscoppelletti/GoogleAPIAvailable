GoogleAPIAvailable
==================

Bug in the the method `makeGooglePlayServicesAvailable` of the object
`GoogleApiAvailability`.

> You have to use a device (or an emulator) without Play Store or Google Play
> Services in order to reproduce the bug.

## Test 1

1. The main activity executes the method `makeGooglePlayServicesAvailable`
inside its method `onCreate`.
2. The following `AlertDialog` appears:

        Google API Available won't run without Google Play services, which are
        not supported by your device.
    
3. Close the dialog by clicking the button `OK` (the only one button available).
4. The `AlertDialog` disappears, but the activity is still active and I can use
the App normally, but eventually the Google APIs will crash.
5. Only if you close the activity (for example, clicking the `Back` button), the
`Task` started by the method `makeGooglePlayServicesAvailable` fails finally
and you can see the following on `logcat`:

        E/MyTest: Task failed.
        java.util.concurrent.CancellationException: Host activity was destroyed before Google Play services could be made available.
        at com.google.android.gms.common.api.internal.zabu.onDestroy(Unknown Source)
        at com.google.android.gms.common.api.internal.zzc.onDestroy(Unknown Source)
        at androidx.fragment.app.Fragment.performDestroy(Fragment.java:2699)
        at androidx.fragment.app.FragmentManagerImpl.moveToState(FragmentManager.java:1591)
        at androidx.fragment.app.FragmentManagerImpl.moveFragmentToExpectedState(FragmentManager.java:1784)
        at androidx.fragment.app.FragmentManagerImpl.moveToState(FragmentManager.java:1852)
        at androidx.fragment.app.FragmentManagerImpl.dispatchStateChange(FragmentManager.java:3269)
        at androidx.fragment.app.FragmentManagerImpl.dispatchDestroy(FragmentManager.java:3260)
        at androidx.fragment.app.FragmentController.dispatchDestroy(FragmentController.java:274)
        at androidx.fragment.app.FragmentActivity.onDestroy(FragmentActivity.java:419)
        at androidx.appcompat.app.AppCompatActivity.onDestroy(AppCompatActivity.java:210)
        at android.app.Activity.performDestroy(Activity.java:5172)
        at android.app.Instrumentation.callActivityOnDestroy(Instrumentation.java:1109)
        at android.app.ActivityThread.performDestroyActivity(ActivityThread.java:3260)
        at android.app.ActivityThread.handleDestroyActivity(ActivityThread.java:3291)
        at android.app.ActivityThread.access$1200(ActivityThread.java:130)
        at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1248)
        at android.os.Handler.dispatchMessage(Handler.java:99)
        at android.os.Looper.loop(Looper.java:137)
        at android.app.ActivityThread.main(ActivityThread.java:4745)
        at java.lang.reflect.Method.invokeNative(Native Method)
        at java.lang.reflect.Method.invoke(Method.java:511)
        at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:786)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:553)
        at dalvik.system.NativeStart.main(Native Method)
        
        W/MyTest: Activity has been completed before the task.

## Test 2

1. The main activity executes the method `makeGooglePlayServicesAvailable`
inside its method `onCreate`.
2. The following `AlertDialog` appears:

        Google API Available won't run without Google Play services, which are
        not supported by your device.

3. Close the dialog by clicking the `Back` button (o by clicking out of the
`AlertDialog`).
4. The `AlertDialog` disappears, and the `Task` started by the method
`makeGooglePlayServicesAvailable` fails immediately and you can see the
following on `logcat`:

        E/MyTest: Task failed.
        com.google.android.gms.common.api.ApiException: 13: 
        at com.google.android.gms.common.internal.ApiExceptionUtil.fromStatus(Unknown Source)
        at com.google.android.gms.common.api.internal.zabu.zaa(Unknown Source)
        at com.google.android.gms.common.api.internal.zal.onCancel(Unknown Source)
        at com.google.android.gms.common.SupportErrorDialogFragment.onCancel(Unknown Source)
        at android.app.Dialog$ListenersHandler.handleMessage(Dialog.java:1229)
        at android.os.Handler.dispatchMessage(Handler.java:99)
        at android.os.Looper.loop(Looper.java:137)
        at android.app.ActivityThread.main(ActivityThread.java:4745)
        at java.lang.reflect.Method.invokeNative(Native Method)
        at java.lang.reflect.Method.invoke(Method.java:511)
        at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:786)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:553)
        at dalvik.system.NativeStart.main(Native Method)
        
        E/MyTest: Google Play services not available.
        com.google.android.gms.common.api.ApiException: 13: 
        at com.google.android.gms.common.internal.ApiExceptionUtil.fromStatus(Unknown Source)
        at com.google.android.gms.common.api.internal.zabu.zaa(Unknown Source)
        at com.google.android.gms.common.api.internal.zal.onCancel(Unknown Source)
        at com.google.android.gms.common.SupportErrorDialogFragment.onCancel(Unknown Source)
        at android.app.Dialog$ListenersHandler.handleMessage(Dialog.java:1229)
        at android.os.Handler.dispatchMessage(Handler.java:99)
        at android.os.Looper.loop(Looper.java:137)
        at android.app.ActivityThread.main(ActivityThread.java:4745)
        at java.lang.reflect.Method.invokeNative(Native Method)
        at java.lang.reflect.Method.invoke(Method.java:511)
        at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:786)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:553)
        at dalvik.system.NativeStart.main(Native Method)

5. Finally my `AlertDialog` appears:

        Google Play service not available.
        
6. Close the dialog and the App will exit.

## Expected behavior

I think the `Task` started by the method `makeGooglePlayServicesAvailable`
should fail after you close the first `AlertDialog` regardless how you close the
dialog. Thus I could terminate the App if I do not want the App runs without
Google Play Services.

