# Scala and Chisel Labs for System Architecture 2024 [![Binder](https://mybinder.org/badge_logo.svg)](https://mybinder.org/v2/git/https%3A%2F%2Fgitlab.cs.uni-saarland.de%2Freineke%2Fsysarch-labs/HEAD?urlpath=tree/labs)

Here, we publish labs on a weekly basis to help you learn Scala and Chisel. 
The labs are intended to be solved alongside the lecture in addition to the voluntary assignment sheet and will not be graded.
The first course project will involve Chisel and the labs are meant to prepare you for that.

If you have questions regarding an exercise, you can post your question on the forum or open a ticket in this GitLab.


### How to run the labs?

There are three ways in which the labs can be run and which are detailed below:
1. Via [Binder](https://mybinder.org/), which provides you with a Jupyter Notebook environment and a copy of this repository in the browser. You do not need to install any software on your own machine.
2. By using Docker and the Dockerfile we provide in the repository.
3. As a last resort, we also provide a script to install all dependencies that you need to run the labs. 

### Binder:

You can launch Binder with the launch button next to the heading or here: 
[![Binder](https://mybinder.org/badge_logo.svg)](https://mybinder.org/v2/git/https%3A%2F%2Fgitlab.cs.uni-saarland.de%2Freineke%2Fsysarch-labs/HEAD?urlpath=tree/labs)

Be aware that changes inside Binder are not persistent and will be gone once the session is terminated!

Also Binder can take quite a while to start up. So you might want to have a coffee after you clicked the link.

### Docker:
Using the Dockerfile in the repository you can locally build a Docker container that resembles the Binder environment.

Using Docker has the advantage that your changes to the Jupyter notebooks are persistent.
It may also be faster than Binder.

Make sure you have [Docker](https://docs.docker.com/get-docker/) installed and system privileges to run a container on your system.

Build the Docker image from the Dockerfile and start a corresponding Docker container as follows:

```docker compose up --build```

You should then be able to access your local notebook from [127.0.0.1:8888](127.0.0.1:8888).

### install.sh:
Before you run this script please check if this script is suitable to your system and if not make changes to it. As you should always do with scripts that you didn't write yourself!!!

When you finished the installation you will be able to activate an environment via `source chisel_nb_env/bin/activate` and then start Jupyter via `jupyter notebook`.

### Acknowledgements

Materials and ideas are borrowed from the labs of the course [CSE 228A - Agile Hardware Design](https://classes.soe.ucsc.edu/cse228a/Winter24/) from [UCSC](https://www.ucsc.edu/). Be aware that lab material can significantly differ, though you might still want to take a look at it out of interest.