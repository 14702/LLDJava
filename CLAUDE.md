# Project conventions

## Code structure
- Separate interfaces and implementations into `interfaces/` and `impl/` subdirectories within each package
- Classes should depend on interfaces, not concrete implementations
- Keep design expandable — use interfaces/abstract classes so new requirements can be added without rewriting core
- Follow OOP/SOLID principles: separation of concerns, single responsibility

## Concurrency (VIMP)
- Use thread-safe collections: ConcurrentHashMap, CopyOnWriteArrayList, etc.
- Use proper locking (ReentrantLock, synchronized) where needed
- Always consider concurrent access in every design

## Design patterns
- Apply design patterns where appropriate (Strategy, Observer, Factory, etc.)
- Prioritize extensibility — new features should plug in, not require rewrites

## Testing
- Tests use plain `main()` with manual assertion helpers (no JUnit)
- Keep tests simple — TDD style, not extensive, just cover core requirements

## Style
- Keep code short and readable, avoid verbosity
- Fulfill all requirements in the simplest way possible
- Don't over-engineer — keep implementations concise

## Documentation
- After implementation is complete, create an interactive HTML architecture diagram for each LLD project
- Diagram should be UML-style class diagram: simple and intuitive to understand
- Show classes with fields and methods, relationships (implements, uses, has-a), and a sequential data flow
- Keep it clean — easy to read at a glance, not cluttered
- Build the diagram at the very end, after all code is done and tested
