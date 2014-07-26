/**
 * Domain services.
 *
 * Domain services are expressed in terms of the ubiquitous language and the domain types, i.e. the method arguments and
 * the return values are proper domain classes. Sometimes, only the service interface (what the service does) is part of
 * the domain layer, but the implementation (how the service does it) is part of the infrastructure layer. This is
 * analogous to how repository interfaces are part of the domain layer, but the Hibernate implementations are not.
 *
 * On the other hand, if the service is possible to implement strictly using the domain layer, both the interface and
 * the implementation could be part of the domain layer.
 *
 * When operation doesn't conceptually belong to any Entity or Value Object, then rather than forcing the behavior into
 * an object, we should create a Domain Service.
 *
 * The interface of a Service should be defined in terms of other elements of the domain model. In other words,
 * parameters and return values of a Service (can/should) be domain objects. This is not a strict constraint, but offers
 * certain advantages. The idea behind the rule is that domain services contain functionality that supplements existing
 * entities and value objects. Another non-strict constraint is closure of operations where both the argument and the
 * return value of domain service methods are of the same type. Both of these constraints promote immutability and a
 * functional style thereby reducing side-effects and making it easier to reason about the code, refactor the code, etc.
 *
 * It is possible to have a domain service method that accepts a primitive type which is neither an entity or
 * value object. However, extensive use of primitive types can result in primitive obsession
 *
 */
package se.inera.fmu.core.domain.service;