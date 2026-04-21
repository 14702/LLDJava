┌─────────────────────────────────────────────────────────────┐
│                         MAIN                                 │
│                                                              │
│  1. SETUP                                                   │
│     ├─ IssueRepository.getInstance() [Singleton]           │
│     ├─ new AgentRepository() [ConcurrentHashMap]           │
│     ├─ new SimpleAgentAssignmentStrategy()                 │
│     └─ new AgentService/IssueService [Dependency Inject]  │
│                                                              │
│  2. CREATE ISSUES (3)                                       │
│     ├─ Issue created (validated)                           │
│     ├─ Stored in IssueRepository                           │
│     └─ Indexed by email & type                             │
│                                                              │
│  3. ADD AGENTS (2)                                          │
│     ├─ Agent created (validated)                           │
│     ├─ Stored in AgentRepository                           │
│     └─ With expertise list                                 │
│                                                              │
│  4. ASSIGN ISSUES                                           │
│     ├─ Strategy checks if agent FREE                       │
│     │  ├─ YES → Assign directly (ASSIGNED status)         │
│     │  └─ NO → Add to waiting queue                        │
│     └─ Update working history                              │
│                                                              │
│  5. FILTER ISSUES                                           │
│     ├─ By email (using index)                              │
│     ├─ By type (using index)                               │
│     └─ By builder (multi-criteria)                         │
│                                                              │
│  6. UPDATE ISSUE                                            │
│     ├─ Get issue from repository                           │
│     ├─ synchronized { change status }                      │
│     └─ OPEN → IN_PROGRESS                                  │
│                                                              │
│  7. RESOLVE ISSUE                                           │
│     ├─ synchronized { mark RESOLVED }                      │
│     ├─ Free agent (set currIssue = null)                   │
│     ├─ Check waiting queue                                 │
│     │  ├─ If empty → Agent is FREE                        │
│     │  └─ If has items → Pick next issue                  │
│     └─ Update working history                              │
│                                                              │
│  8. VIEW HISTORY                                            │
│     └─ For each agent: print all worked issues             │
└─────────────────────────────────────────────────────────────┘
