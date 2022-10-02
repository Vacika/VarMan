**Useful information:** <br/>
<ul>
<li>List variables for environment:<pre>
https://api.bitbucket.org/2.0/repositories/{workspace}/{repo_slug}/deployments_config/environments/{environment_uuid}/variables</pre> </li>
<li>List environments:<pre> https://api.bitbucket.org/2.0/repositories/{workspace}/{repo_slug}/environments/</pre></li>
<li>Create variable:
<pre>curl --request POST \
  --url 'https://api.bitbucket.org/2.0/repositories/{workspace}/{repo_slug}/deployments_config/environments/{environment_uuid}/variables' \
  --header 'Accept: application/json' \
  --header 'Content-Type: application/json' \
  --data '{
  "type": "<string>",
  "uuid": "<string>",
  "key": "<string>",
  "value": "<string>",
  "secured": true
}'
</pre>
<li>Delete a variable:<pre>
curl --request DELETE \
  --url 'https://api.bitbucket.org/2.0/repositories/{workspace}/{repo_slug}/deployments_config/environments/{environment_uuid}/variables/{variable_uuid}'
</pre></li>

</li>
<li>Update Variable: <pre>curl --request PUT \
  --url 'https://api.bitbucket.org/2.0/repositories/{workspace}/{repo_slug}/deployments_config/environments/{environment_uuid}/variables/{variable_uuid}' \
  --header 'Accept: application/json' \
  --header 'Content-Type: application/json' \
  --data '{
  "type": "<string>",
  "uuid": "<string>",
  "key": "<string>",
  "value": "<string>",
  "secured": true
}
</pre></li>

</ul>

# VarMan

![Build](https://github.com/Vacika/VarMan/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)

## Template ToDo list
- [x] Create a new [IntelliJ Platform Plugin Template][template] project.
- [ ] Get familiar with the [template documentation][template].
- [ ] Verify the [pluginGroup](./gradle.properties), [plugin ID](./src/main/resources/META-INF/plugin.xml) and [sources package](./src/main/kotlin).
- [ ] Review the [Legal Agreements](https://plugins.jetbrains.com/docs/marketplace/legal-agreements.html).
- [ ] [Publish a plugin manually](https://plugins.jetbrains.com/docs/intellij/publishing-plugin.html?from=IJPluginTemplate) for the first time.
- [ ] Set the Plugin ID in the above README badges.
- [ ] Set the [Deployment Token](https://plugins.jetbrains.com/docs/marketplace/plugin-upload.html).
- [ ] Click the <kbd>Watch</kbd> button on the top of the [IntelliJ Platform Plugin Template][template] to be notified about releases containing new features and fixes.

<!-- Plugin description -->
This Fancy IntelliJ Platform Plugin is going to be your implementation of the brilliant ideas that you have.

This specific section is a source for the [plugin.xml](/src/main/resources/META-INF/plugin.xml) file which will be extracted by the [Gradle](/build.gradle.kts) during the build process.

To keep everything working, do not remove `<!-- ... -->` sections.
<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "VarMan"</kbd> >
  <kbd>Install Plugin</kbd>

- Manually:

  Download the [latest release](https://github.com/Vacika/VarMan/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
