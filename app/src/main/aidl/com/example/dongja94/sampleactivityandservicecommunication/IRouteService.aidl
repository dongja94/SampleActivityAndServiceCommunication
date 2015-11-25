// IRouteService.aidl
package com.example.dongja94.sampleactivityandservicecommunication;
import com.example.dongja94.sampleactivityandservicecommunication.IRouteCallback;

interface IRouteService {
    boolean startRouting(double lat, double lng, int endCount);
    boolean registerCallback(IRouteCallback callback);
    boolean unregisterCallback(IRouteCallback callback);
}
