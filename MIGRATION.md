# Migration Guide: New Architecture Support

This guide explains the migration from the old ViewManager-based implementation to the new composition-based approach that supports React Native's New Architecture (Fabric).

## What Changed

### Previous Implementation (v0.1.7 and earlier)

The library previously used a **ViewManager-based approach**:
- Extended `RNCWebViewManager` on both iOS and Android
- Used `nativeConfig={{ component: HyperWebViewManager }}` to substitute the native component
- **Only compatible with Old Architecture**

### New Implementation (v1.0.0+)

The library now uses a **composition-based approach**:
- Uses `react-native-webview` directly (which supports both architectures)
- Integrates HyperWebView services through a native module
- **Compatible with both Old and New Architecture*

**✅ No API changes required!** The component interface remains the same.

### Native Implementation Changes

#### iOS Changes

**Before:**
- `HyperWebViewManageriOS` ViewManager extending `RNCWebViewManager`
- Exported via `RCT_EXTERN_MODULE`

**After:**
- `HyperWebViewModule` native module
- Integrates with any WebView via React tag lookup
- ViewManager kept for backward compatibility but not used

#### Android Changes

**Before:**
- `HyperWebViewManager` extending `RNCWebViewManager`
- Direct WebView wrapping

**After:**
- `HyperWebViewModule` native module
- Integrates with any WebView via view hierarchy lookup

## Migration Steps

### 1. Update Your React Native Project

Ensure your project supports the version of react-native-webview used:

```json
{
  "dependencies": {
    "react-native-webview": ">=12.0.0"
  }
}
```

### 2. Enable New Architecture (Optional)

If you want to use New Architecture, update your configuration:

#### iOS (Podfile)
```ruby
use_react_native!(
  :path => config[:reactNativePath],
  :fabric_enabled => true,
  :new_arch_enabled => true
)
```

#### Android (gradle.properties)
```properties
newArchEnabled=true
```

### 3. Rebuild Your Project

```bash
# iOS
cd ios && pod install && cd ..
npx react-native run-ios

# Android
npx react-native run-android
```

## Compatibility

| Version | Old Architecture | New Architecture | React Native |
|---------|------------------|------------------|--------------|
| v0.1.7 and earlier | ✅ | ❌ | 0.60+ |
| v1.0.0+ | ✅ | ✅ | 0.60+ |

## Troubleshooting

### "Module HyperWebViewModule not found"

This error indicates the native module isn't properly linked:

1. **iOS**: Run `cd ios && pod install`
2. **Android**: Ensure the package is added to `MainApplication.java`
3. Rebuild the project

### WebView not initializing

If HyperWebView services aren't attaching:

1. Check that `iframeIntegration` prop is set correctly
2. Verify WebView is rendered before initialization
3. Check React Native logs for detailed error messages

### Performance Considerations

The new implementation:
- ✅ **Slightly better performance** - no ViewManager overhead
- ✅ **Better memory management** - automatic cleanup
- ✅ **More reliable** - uses official React Native APIs

## Backward Compatibility

The library maintains backward compatibility:
- Old iOS ViewManager code is preserved but unused
- Old Android ViewManager code is preserved but unused
- No changes required to existing user code

## Need Help?

If you encounter issues during migration:

1. Check the [troubleshooting section](#troubleshooting)
2. Review your React Native and react-native-webview versions
3. Create an issue on GitHub with:
   - React Native version
   - New Architecture enabled/disabled
   - Complete error logs
   - Minimal reproduction example