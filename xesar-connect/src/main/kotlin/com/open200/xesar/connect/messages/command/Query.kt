package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.FilterTypeSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.UUID
import kotlinx.serialization.Serializable

/**
 * Represents a query command.
 *
 * @property resource The resource to query.
 * @property requestId The unique identifier for the request.
 * @property token The token used for authentication.
 * @property id The unique identifier of an item to query (optional).
 * @property params The parameters for the query (optional).
 */
@Serializable
class Query(
    val resource: String,
    @Serializable(with = UUIDSerializer::class) val requestId: UUID,
    val token: String,
    @Serializable(with = UUIDSerializer::class) val id: UUID?,
    val params: Params? = null
) : Command {

    /**
     * Represents the parameters for a query.
     *
     * @property pageOffset The offset of the page to retrieve (optional).
     * @property pageLimit The limit of results per page (optional).
     * @property sort The sorting criteria for the results (optional).
     * @property language The language for localization (optional).
     * @property filters The list of filters to apply (optional).
     */
    @Serializable
    data class Params(
        val pageOffset: Int? = null,
        val pageLimit: Int? = null,
        val sort: String? = null,
        val language: String? = null,
        val filters: List<Filter>? = null
    ) {
        /**
         * Represents a filter to apply in a query.
         *
         * @property field The field to filter on (optional).
         * @property type The type of filter to apply (optional).
         * @property value The value to filter by (optional).
         */
        @Serializable
        data class Filter(
            val field: String? = null,
            @Serializable(with = FilterTypeSerializer::class) val type: FilterType? = null,
            val value: String? = null
        )
    }
}
