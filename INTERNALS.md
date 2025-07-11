This is a big-picture explaination of how (the implemented parts of) git works. 
The explaination here would be top-bottom approach not bottom-up for people, like me, who likes the declarative code more

# The data model for working locally
<ul> 
<li>Each object in git has a unique 40 charachters (SHA-1) identifier. </li>

<li>Objects reference each other by that identifier.</li>
<li>At each point a commit is made, git takes a snapshot of the staging area and make a commit object referencing it</li>

</ul>

## But how that snapshot is being made? 
Any directory has two types of data
<ul>
  <li>Files</li>
  <li>Directories</li>
</ul>
And that exactly what git has objects for.
For the first one, git has blob objects.  And for the second we have tree objects. 

Just as directories can be nested, trees can be nested. 
There is, however, one important difference between directories and trees: There shall be no empty trees (Trees not referencing some blobs after an arbitairly number of traversals).  The motivations behind that is pretty straightforward: A version control is only interested in the changes.

Now we have a snapshot of the working directory represented as one tree object with references, we still need to maintain different snapshots. And thats exactly what a commit is for. 
It stores some metainfo about the commit like the message and commiter, and maintain two references: 
<ui>
<li>The tree of the working directory</li>
<li>The previous commit</li>
</ui>
That way, we can traverse the whole history of a git directory. 

# So far so good, but how do you work with remotes ? 
The communication between clients (sth like our local repo) and servers (like github or gitlab) can be done via different protocols, like http, git or ssh protocol. 

At the high level, we have different histories for each of the client and server. They both negotiate what each of them have, and try to figure out the minimal set of objects to send. 

## git clone 
The most basic scenario is when the server have some objects and the client have none. The server then sends all available objects to the client. 

At this point the client would populate all the objects to the database and have the exact same history as the server has
## git pull
It is essentially the same as **git clone**, but now the client has already some commits, so the server figures out the minimal required commits the client needs (everything after its **HEAD**) then sends them. At this point, it is analouge to the clone process. 


