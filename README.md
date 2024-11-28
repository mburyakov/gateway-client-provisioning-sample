This project shows the example of how a specific plugin can be delivered to all the
installations of JetBrains Client managed by JetBrains Gateway. The solution consists of two parts:
- `payload-plugin` is a plugin that needs to be installed to the client of remote development setup.
  It's empty in this example and should be replaced with the actual plugin that is going to be installed to clients.
- `gateway-hook` is a plugin to JetBrains Gateway which carries a zip file with `payload-plugin` inside as a resource
  and unpacks it to all the clients managed by the Gateway.

IDE Services can be used to spread the `payload-plugin` across all the Remote Development clients withing the organization.
For that purpose, the outer plugin (`gateway-hook`) should be uploaded to IDES server
as an [autoinstalled plugin](https://www.jetbrains.com/help/ide-services/manage-available-plugins.html#auto-installed_plugins).
[Application filter](https://www.jetbrains.com/help/ide-services/manage-available-plugins.html#filters_for_auto-installed_plugins)
can be used to avoid installing the `gateway-hook` plugin to the tools other than Gateway.

The solution uses the extension point `com.intellij.remoteDev.downloader.ConfigureClientHook` in Gateway which is called every time before a Gateway launches a client,
be it the first time after downloading the client, or subsequent launches of the already installed client.
The extension point appeared in Gateway 2024.3. The version of the actual IDE used for RD can be lower though.
It's up to the extension implementation to check (if needed) the version of the client which is going to be launched.
For example, if `payload-plugin` shouldn't be installed to some specific tools or versions, unpacking can be skipped
if the detected version of a client is not supported.
Also, there is a known limitation that the IDE used for RD shouldn't be IntelliJ IDEA Community or PyCharm Community:
only Ultimate/Professional editions are supported for RD.

To build the project, use `:gateway-hook:buildPlugin` task.