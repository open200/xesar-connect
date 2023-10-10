/**
 * Marker interface for a query resource. Implementing this interface indicates that a class is a
 * query resource.
 */
interface QueryResource
/**
 * Marker interface for a query resource that represents a list of elements. Implementing this
 * interface indicates that a class is a query resource for a list response. It extends the
 * [QueryResource] interface.
 */
interface QueryListResource : QueryResource

/**
 * Marker interface for a query resource that represents a single element. Implementing this
 * interface indicates that a class is a query resource for an element response. It extends the
 * [QueryResource] interface.
 */
interface QueryElementResource : QueryResource
