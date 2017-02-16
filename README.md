# mainframer-intellij-plugin [![Build Status](https://travis-ci.org/elpassion/mainframer-intellij-plugin.svg?branch=master)](https://travis-ci.org/elpassion/mainframer-intellij-plugin) [![Stories in Ready](https://badge.waffle.io/elpassion/mainframer-intelij-plugin.svg?label=ready&title=Ready)](http://waffle.io/elpassion/mainframer-intelij-plugin)
An IntelliJ IDEA plugin for [Mainframer](https://github.com/gojuno/mainframer).

About
-----

This plugin makes integration with Mainframer tool easy and seamless. Plugin uses IntelliJ IDEA run configuration mechanism, especially it takes advantage of before run tasks, to replace standard build tasks with the one using mainframer. In addition it provides new runConfiguration to run mainframer tasks.

#### Features
<ul>
  <li>BeforeTasks which delegate tasks to mainframer</li>
  <li>Injecting/Restoring before tasks to all configurations</li>
  <li>Configuring mainframer in project</li>
  <li>RunConfiguration for mainframer</li>
</ul>

Installation
------------

1. Open plugins window (with proper action or through *Preferences > Plugins*).
2. Find **mainframer-plugin** in JetBrains plugins repository.
3. Install plugin and restart IDE to apply changes.

**Alternative way:**

If you want to install a specific version of the plugin visit [mainframer-plugin website](https://plugins.jetbrains.com/idea/plugin/9447-mainframer-plugin), download zip file and click *Install plugin from disk...* providing path to it.

![](readme/plugins.png)

Usage
-----

### Actions

Launch *Enter action dialog* with (<kbd>command</kbd> or <kbd>ctrl</kbd>) + <kbd>shift</kbd> + <kbd>A</kbd> and find *Mainframer* action. Then press *Enter* to show group of all actions available with plugin:

![](readme/tasks.png)

#### Configure Mainframer in Project

This is an initial action you need to perform to configure Mainframer in your current project. It starts with fetching a list of all available tool releases. You can select a Mainframer tool version you are interested in. It will check if your opened project contains a *mainframer.sh* file and if not it will download it for you.

#### Inject mainframer before run task

With this action plugin will modify all your created run configurations. For each configuration it will remove default *before launch* task and inject **MainframerBefore** task:

![](readme/injecting.png)

#### Restore default before run tasks

Whenever you decide to build your project locally, this action will restore default *before run* tasks in all run configurations defined.