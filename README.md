
# Mr Wrapper

Love kotlin? Started as the authentication server for my personal needs. But I extended it to do all other things of the world, with the beautiful language called **Kotlin**., all in wrapped under a single package.

## Deployment

Build the project once in Intellij iDEA or in Android Studio, then grab the ```*-all.jar``` under the folder ```MrWrapper\build\libs```

Copy paste the jar into your favourite cloud provider
(suppose you place the jar in the folder ```backend```).

Create a `env.sh` file containing:
```bash
export MONGO_PW="qw48uoqwirehoiqhublahblah"
export JWT_SECRET="a lenghty secret you choose to put here.mp3"
```
Run the script:
```bash
chmod +x $HOME/backend/env.sh
source $HOME/backend/env.sh
/usr/local/bin/java java -jar $HOME/backend/one.karaage.mrwrapper-all.jar
```
Port Configuration is necessary, so do edit `MrWrapper\src\main\resources\application.conf`
### Quick Note
- Linux: you can optionally write a `systemd` service to restart your backend after system failue
- FreeBSD: you can optionally write a `rc` file to ensure high availability
- Or, write a **cron job** to register above deployment commands with `@reboot` flag and a script that checks the application's operation to restarts if necessary (W.I.P)
- Don't forget to create your CA certificate (for https)
## Used By

This project is used by :

- Just me
- Again me


## Special Mentions

- [Project Generator](https://start.ktor.io/)
- [Authentication](https://youtu.be/uezSuUQt6DY)
