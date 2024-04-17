# Labs [![Binder](https://mybinder.org/badge_logo.svg)](https://mybinder.org/v2/git/https%3A%2F%2Fgitlab.cs.uni-saarland.de%2Freineke%2Fsysarch-labs/HEAD)

The here published labs offer current tasks for the lecture.
They are intended to be solved alongside the lecture in addition to the voluntary assignment sheet.

We don't plan to publish any solutions to the tasks since most tasks are tested inline for correctness so you can test yourself.

If you have questions regarding an exercise, you can raise your question on the forum or open a ticket in this GitLab.

### How to run?

We recommend using [Binder](https://mybinder.org/), it provides you with a Jupyter Notebook environment and a copy of this repository. You don't have to install anything.

Another way of running the labs is using the Dockerfile we provide in the repository to build a container similar to the Binder environment.\
Make sure you have Docker installed and system privileges to run a container on your system.

As last resort, we also provide a script to install all dependencies that you need to run the labs. Before you run this script please check if this script is suitable to your system and if not make changes to it. As you should always do with scripts that you didn't write yourself!!!

- #### Binder:
    You can launch Binder with the launch button next to the heading or here: [![Binder](https://mybinder.org/badge_logo.svg)](https://mybinder.org/v2/git/https%3A%2F%2Fgitlab.cs.uni-saarland.de%2Freineke%2Fsysarch-labs/HEAD)

    Be aware that Binder will discard any work after 10 minutes of idle time. So you might want to switch to your local version at sometime, so you don't lose all your hard work.

    Also this can take quite a while to start up. So you might want to make your coffee after you clicked the link.

- #### Docker:
    Build the Dockerfile like this:

    ```
    docker build --tag 'labs' .
    ```

    And then run it like this:

    ```
    docker run -p 8888:8888 labs:latest
    ```

    You should then be able to access your local notebook from [127.0.0.1:8888](127.0.0.1:8888).

- #### install.sh:
    As stated above make sure you know what you are doing, before you run this script.\
    When you finished the installation you will be able to activate an environment via `source chisel_nb_env/bin/activate` and then start Jupyter via `jupyter notebook`.

### Acknowledgements

Materials and ideas are borrowed from the labs of the course [CSE 228A - Agile Hardware Design](https://classes.soe.ucsc.edu/cse228a/Winter24/) from [UCSC](https://www.ucsc.edu/). Be aware that lab material can significantly differ, though you might still want to take a look at it out of interest.