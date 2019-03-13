#### What assumptions did you make?
- My API can have only one client
- According to the given hint I suggested that the order of items in the Amazon API response is not significant
- I calculated a score for an exact keyword, e.g. `iphone` has score = 0 because it does not appear in autocomplete results  

#### How does your algorithm work?

I split the given keyword into substrings. 
Each substring starts with the first letter and has the length from 1 to N, where N is the length of the keyword. 
Then I send requests with substrings to Amazon autocomplete API and compare results.

I calculate the score as `(1 - A / B) * 100` where\
`A` = the longest substring of the given keyword which does not appear yet in the autocomplete results\
`B` = keyword length

I created two services - Sequential Score Service and Concurrent Score Service. 

The Sequential Score Service sends requests sequentially and stops sending when returned autocompleted 
set contains the keyword or the current substring is equal to the keyword.\
This solution is simple and reliable, but not optimal.

The Concurrent Score Service invokes multiple simultaneous requests to the autocomplete API with each substring.
It can stop sending requests if the result is already calculated.\
This solution is faster but has a drawback - 
this service can not be used by multiple clients at the same time, i.e. it is not thread-safe.

#### Do you think the (*hint) that we gave you earlier is correct and if so - why?
As I can see from responses an order of the 10 returned keywords is always the same, 
but I can not be sure about the algorithm Amazon uses.
Anyway having an ordered list would help to calculate more precise score.

#### How precise do you think your outcome is and why?
- My algorithm has a simple formula, but it seems to consider common edge cases.
- My algorithm calculates the popularity of the given keyword only among other keywords
starting with the same letter, i.e. I can not compare scores of `aaa` and `bbb`, 
but can only compare scores of `aaa`, `aab`, `abc`...
- My algorithm works more precise for long keywords rather than short