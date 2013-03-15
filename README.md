Libkit
=============
Liberate Bukkit (Libkit for short) is a Bukkit API fork that has enhanced features and optimizations as well as compability adjustments to allow older plugins that do not support the new namespace version system that Bukkit put into place.

## Contributing
Are you a talented programmer looking to contribute some code? We'd love the help!
* Any section inside OBC or NMS should be commented with "Libigot start" or "Libigot end", unless it belongs to a single line, in which case "Libigot" should be commented next to that line.
* The code you write should be as clean as possible.
* If you have to unshade classes first add them unchanged with a commit message like "Add <classname> for diff visibility.", then make your changes with the next commit.
* You should only modify something if it is server-side. If the change you make affects client-side, it will not work, and it should be denied/removed.
* All commits should have a title and a bulleted description with each of the added features and/or restrictions.

## The license
Libkit and Libigot are licensed under the [GPLv3 License][License].

## Getting the source
The latest and greatest source can be found here on [GitHub][Source].

If you are using Git, use this command to clone the project:

    git clone git://github.com/Libigot/Libkit.git

## Compiling the source
Libkit uses Maven to handle its dependencies.

* Install [Maven 2 or 3](http://maven.apache.org/download.html)  
* Checkout this repo and run: `mvn clean install`

## Learn More
* [Homepage]
* [Source]
* [Builds]
* [Issues]
* [License]
* [IRC] - #libigot on irc.esper.net

[Homepage]: http://www.libigot.org
[License]: http://www.gnu.org/licenses/gpl.html
[Source]: https://github.com/Libigot/Libigot.git
[Libkit]: http://github.com/Libigot/Libkit
[Builds]: http://build.libigot.org
[Issues]: https://github.com/Libigot/Libigot/issues
[IRC]: http://libigot.org/pages/chat/
