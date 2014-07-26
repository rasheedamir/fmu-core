/**
 * Created by Rasheed on 7/5/14.
 *
 * Application Services
 *
 * The application services are responsible for driving workflow and coordinating transaction management (by use of the
 * declarative transaction management support in Spring). They also provide a high-level abstraction for clients to use
 * when interacting with the domain. These services are typically designed to define or support specific use cases.
 *
 * In some situations, e.g. when dealing with graphs of lazy-loaded domain objects or when passing services' return
 * values over network boundaries, the services are wrapped in facades. The facades handle ORM session management issues
 * and/or convert the domain objects to more portable Data Transfer Objects) that can be tailored to specific use cases.
 * In that case, we consider the DTO-serializing facade part of the interfaces layer.
 *
 */
package se.inera.fmu.core.application;