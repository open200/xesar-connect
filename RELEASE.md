# Release Guide

This document describes how to publish xesar-connect to Maven Central via Sonatype.

## Prerequisites (One-Time Setup)

### 1. Install GPG

```bash
brew install gpg
```

### 2. Import GPG Keys

Import your public and private keys, then export the secret keyring:

```bash
gpg --import public-key.asc
gpg --import private-key.asc
gpg --list-secret-keys  # note the last 8 characters of the key ID
gpg --export-secret-keys -o ~/.gnupg/secring.gpg
```

### 3. Configure Gradle Properties

Obtain the Sonatype credentials and GPG passphrase from our OpenBao instance.

Add the following to `~/.gradle/gradle.properties`:

```properties
ossrhUsername=<sonatype-username>
ossrhPassword=<sonatype-password>
signing.keyId=<last-8-chars-of-gpg-key-id>
signing.password=<gpg-passphrase>
signing.secretKeyRingFile=/Users/<you>/.gnupg/secring.gpg
```

## Release Process

### 1. Check and Set the Release Version

First, check the current version in `gradle.properties`. It should look like this:

```properties
version=2.2.0-SNAPSHOT
```

Remove the `-SNAPSHOT` suffix to set the release version:

```properties
version=2.2.0
```

### 2. Run Tests and Build the Artifact

```bash
./gradlew spotlessApply test prepareMavenPublish
```

This will:
- Format the code
- Run all tests
- Build, sign, and publish artifacts to `build/xesar-connect-artifact/`
- Create a ZIP archive in `build/`

### 3. Upload to Sonatype

Manually upload the generated ZIP file from the `build/` directory to the [Sonatype Central Portal](https://central.sonatype.com/).

### 4. Tag the Release

```bash
git commit -am "release: v2.2.0"
git tag v2.2.0
git push origin main --tags
```

### 5. Bump to Next Development Version

In `gradle.properties`, bump the version and add the `-SNAPSHOT` suffix back:

```properties
version=2.2.1-SNAPSHOT
```

This ensures that ongoing development builds are clearly marked as unreleased snapshots.

```bash
git commit -am "chore: bump version to 2.2.1-SNAPSHOT"
git push origin main
```
