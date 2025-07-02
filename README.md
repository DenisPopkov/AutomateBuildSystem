# AutomateBuildSystem

## MSI Installer Issue Fix

### Problem
The MSI installer may fail to find `minizip.dll` when installed on Windows 10 systems with non-ASCII characters in the installation path.

### Solution
The build configuration has been updated to:
1. Force ASCII-only installation paths
2. Add proper encoding settings for MSI packaging
3. Include additional MSI configuration options
4. Set proper system encoding during build

### Build Instructions
To build the MSI installer with the fix:

```bash
./gradlew :composeApp:createDistributable
```

The MSI file will be generated in `composeApp/build/compose/binaries/main/app/msi/`

### Installation Notes
- Install to a path with ASCII characters only (e.g., `C:\Program Files\BuildBuild` instead of `C:\Program Files\BuildBuild中文`)
- The installer will now warn users about non-ASCII path issues
