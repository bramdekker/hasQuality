# The hasQuality tool

The hasQuality tool is an open-source command line application that analyzes and reports on the quality of Haskell projects. It can help Haskell programmers assess the quality of the code that they have written. It can also be used to identify anomalous modules or functions in the project. 


## How to use this application

1. Run the application:
```shell
./gradlew run --args="<flags> <path-to-haskell-project-or-file>"
```

Multiple flags can be used to compute and report on multiple different metric categories. The path must point to an existing Haskell file or project containing at least one .hs file.

### Flags

If no flags are used, all metrics are computed and shown in the report.

Flags:<br/>
-sz &emsp;&emsp;&emsp;&emsp; Compute and report on size metrics.<br/>
-rc &emsp;&emsp;&emsp;&emsp; Compute and report on recursion metrics.<br/>
-p  &emsp;&emsp;&emsp;&emsp; Compute and report on pattern metrics.<br/>
-rd &emsp;&emsp;&emsp;&emsp; Compute and report on readability metrics.<br/>
-h &emsp;&emsp;&emsp;&emsp; Compute and report on Halstead metrics.<br/>
-st &emsp;&emsp;&emsp;&emsp; Compute and report on structural metrics.<br/>
-c &emsp;&emsp;&emsp;&emsp; Compute and report on callgraph metrics.<br/>
--help &emsp;&emsp;&emsp; Show this help message

### Metrics

The following metrics are implemented per metric category:

#### Size metrics

- **LOC**: Lines of code, blank lines not included
- **NCLOC**: Non-commented lines of code, blank lines and comment lines not included
- **CLOC**: Comment lines of code
- **ES**: Executable statements (data declarations and headings not included)
- **DSI**: Delivered source instructions
- **Blank lines**: Number of empty lines
- **Size in bytes**: the required storage space in bytes
- **Size in characters**: the number of characters
- **Parse tree size**: number of nodes in the parse tree

If there are multiple modules/files in the project, the following metrics are also calculated:

- **Average module size**: the average NCLOC per module
- **Maximum module size**: the maximum NCLOC of a module
- **Module size inequality**: [Gini-coefficient](https://en.wikipedia.org/wiki/Gini_coefficient) on the NCLOC of the modules

#### Recursion metrics

- **Number of recursive functions**: total number of recursive functions
- **Number of trivial recursive functions**: number of trivial recursive functions
- **Number of non-trivial recursive functions**: number of non-trivial recursive functions
- **Longest non-trivial call chain**: length of the longest non-trivial recursive call chain
- **Ratio recursive/non-recursive functions**: number of recursive function / total number of functions
- **Maximum number of recursive functions**: maximum number of recursive functions in a single module

#### Pattern metrics

- **Average number of variables**: Average number of variables in a pattern
- **Average number of constructors**: Average number of constructors in a pattern
- **Average number of wildcards**: Average number of wildcards in a pattern
- **Wildcard-variables ratio**: Total number of wildcards in patterns / total number of variables in patterns
- **Average depth of nesting**: Average depth of nesting in patterns
- **Maximum depth of nesting**: Maximum depth of nesting in a pattern
- **Average sum of depth of nesting**: Average sum of depths of nesting in patterns
- **Maximum sum of depth of nesting**: Maximum sum of depth of nesting in a pattern
- **Average pattern size**: Average pattern size in parse tree nodes
- **Maximum pattern size**: Maximum pattern size in parse tree nodes

#### Readability metrics

- **Comment density**: CLOC / LOC
- **[Gunning's fog index](https://en.wikipedia.org/wiki/Gunning_fog_index) on comments**: index of how difficult it is to read/understand a piece of text

#### [Halstead metrics](https://en.wikipedia.org/wiki/Halstead_complexity_measures)

- **Halstead length**: total number of operators + total number of operands
- **Halstead vocabulary**: number of unique operators + number of unique operands
- **Halstead volume**: Halstead length x log2(Halstead vocbulary)

If there are multiple modules/files in the project, the following metrics are also calculated:

- **Average Halstead length**: Average Halstead length of all modules
- **Maximum Halstead length**: Maximum Halstead length of a module
- **Average Halstead vocabulary**: Average Halstead vocabulary of all modules
- **Maximum Halstead vocabulary**: Maximum Halstead vocabulary of a module
- **Average Halstead volume**: Average Halstead volume of all modules
- **Maximum Halstead volume**: Maximum Halstead volume of a module

#### Structural metrics

- **Average McCabe's cyclomatic complexity**: Average [cyclomatic complexity](https://en.wikipedia.org/wiki/Cyclomatic_complexity) of all functions
- **Maximum McCabe's cyclomatic complexity**: Maximum cyclomatic complexity of a function
- **Average number of operators per function**: Total number of operators / total number of functions
- **Maximum number of operators**: Maximum number of operators in a function
- **Average number of operands per function**: Total number of operands / total number of functions
- **Maximum number of operands**: Maximum number of operands in a function
- **Modules / functions**: Total number of modules / total number of functions
- **Modules / type synonyms**: Total number of modules / total number of type synonyms
- **Modules / data types**: Total number of modules / total number of data types

If there are multiple modules/files in the project, the following metrics are also calculated:

- **Average number of functions per module**: Average number of functions per module
- **Maximum number of functions in a module**: Maximum number of functions in a module
- **Minimum number of functions in a module**: Minimum number of functions in a module
- **Average number of type synonyms per module**: Average number of type synonyms per module
- **Maximum number of type synonyms in a module**: Maximum number of type synonyms in a module
- **Minimum number of type synonyms in a module**: Minimum number of type synonyms in a module
- **Average number of data types per module**: Average number of data types per module
- **Maximum number of data types in a module**: Maximum number of data types in a module
- **Minimum number of data types in a module**: Minimum number of data types in a module

#### Callgraph metrics

- **Internal reuse**: number of edges - number of nodes + 1 (on undirected module [dependency graph](https://en.wikipedia.org/wiki/Dependency_graph))
- **Average strongly connected component size**: The average number of nodes in all strongly connected subgraphs of the callgraph (a directed [callgraph](https://en.wikipedia.org/wiki/Call_graph) on functions)
- **Maximum strongly connected component size**: The maximum number of nodes of a strongly connected component
- **Average indegree**: Average number of incoming edges on all nodes
- **Maximum indegree**: Maximum number of incoming edges on a node
- **Average outdegree**: Average number of outgoing edges on all nodes
- **Maximum outdegree**: Maximum number od outgoing edges on a node
